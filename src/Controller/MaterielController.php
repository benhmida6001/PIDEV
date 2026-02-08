<?php

namespace App\Controller;

use App\Entity\Materiel;
use App\Form\MaterielType;
use App\Repository\MaterielRepository;
use App\Repository\CategoryRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\String\Slugger\SluggerInterface;


#[Route('/materiel')]
class MaterielController extends AbstractController
{
    #[Route('/', name: 'app_materiel_index', methods: ['GET'])]
    public function index(
        MaterielRepository $materielRepository, 
        CategoryRepository $categoryRepository,
        EntityManagerInterface $entityManager
    ): Response
    {
        $currentUser = $this->getUser();
        $isAdmin = $currentUser && in_array('ROLE_ADMIN', $currentUser->getRoles());

        // Get all materials based on user role and ownership
        if ($isAdmin) {
            // Admin sees everything
            $materiels = $materielRepository->findAll();
            
            // Get unique users who own materials
            $users = $materielRepository->createQueryBuilder('m')
                ->select('DISTINCT IDENTITY(m.owner) as userId, u.email')
                ->join('m.owner', 'u')
                ->orderBy('u.email', 'ASC')
                ->getQuery()
                ->getResult();
        } else {
            // Regular users see:
            // 1. All visible materials from the community
            // 2. All their own materials (visible or not)
            $materiels = $materielRepository->createQueryBuilder('m')
                ->where('m.visible = true')
                ->orWhere('m.owner = :user')
                ->setParameter('user', $currentUser)
                ->orderBy('m.id', 'DESC')
                ->getQuery()
                ->getResult();
            
            $users = [];
        }

        // Get all categories
        $categories = $categoryRepository->findAll();
 // Get operations/demands for the current user
 $myDemands = [];
 $demandsOnMyItems = [];
 
 if ($currentUser) {
     // Get operations where current user is the requester (my demands)
     $myDemands = $entityManager->getRepository(\App\Entity\Operation::class)
         ->createQueryBuilder('o')
         ->where('o.requester = :user')
         ->setParameter('user', $currentUser)
         ->orderBy('o.createdAt', 'DESC')
         ->setMaxResults(5)
         ->getQuery()
         ->getResult();

     // Get operations on materials owned by current user (demands on my items)
     $demandsOnMyItems = $entityManager->getRepository(\App\Entity\Operation::class)
         ->createQueryBuilder('o')
         ->join('o.materiel', 'm')
         ->where('m.owner = :user')
         ->andWhere('o.requester != :user')
         ->setParameter('user', $currentUser)
         ->orderBy('o.createdAt', 'DESC')
         ->setMaxResults(5)
         ->getQuery()
         ->getResult();
 }

 return $this->render('materiel/index.html.twig', [
     'materiels' => $materiels,
     'categories' => $categories,
     'users' => $users,
     'isAdmin' => $isAdmin,
     'myDemands' => $myDemands,
     'demandsOnMyItems' => $demandsOnMyItems,
 ]);
}

    #[Route('/new', name: 'app_materiel_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager, SluggerInterface $slugger): Response
    {
        $materiel = new Materiel();
        $form = $this->createForm(MaterielType::class, $materiel);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Handle image upload
            $imageFile = $form->get('image')->getData();
            if ($imageFile) {
                $originalFilename = pathinfo($imageFile->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename.'-'.uniqid().'.'.$imageFile->guessExtension();

                try {
                    $imageFile->move(
                        $this->getParameter('uploads_directory'),
                        $newFilename
                    );
                    $materiel->setImage($newFilename);
                } catch (FileException $e) {
                    $this->addFlash('error', 'Échec du téléchargement de l\'image');
                }
            }

            // Set owner to current user
            $materiel->setOwner($this->getUser());

            $entityManager->persist($materiel);
            $entityManager->flush();

            $this->addFlash('success', 'Matériel créé avec succès!');
            return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('materiel/new.html.twig', [
            'materiel' => $materiel,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_materiel_show', methods: ['GET'])]
    public function show(Materiel $materiel): Response
    {
        // Check if user can view this material
        $currentUser = $this->getUser();
        $isAdmin = $currentUser && in_array('ROLE_ADMIN', $currentUser->getRoles());
        $isOwner = $currentUser && $materiel->getOwner() === $currentUser;

        // Only owner, admin, or visible materials can be viewed
        if (!$materiel->isVisible() && !$isOwner && !$isAdmin) {
            $this->addFlash('error', 'Ce matériel n\'est pas accessible');
            return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('materiel/show.html.twig', [
            'materiel' => $materiel,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_materiel_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Materiel $materiel, EntityManagerInterface $entityManager, SluggerInterface $slugger): Response
    {
        // Check if current user is the owner
        if ($materiel->getOwner() !== $this->getUser()) {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à modifier ce matériel');
            return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
        }

        $form = $this->createForm(MaterielType::class, $materiel);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Handle image upload
            $imageFile = $form->get('image')->getData();
            if ($imageFile) {
                // Delete old image if exists
                if ($materiel->getImage()) {
                    $oldImagePath = $this->getParameter('uploads_directory') . '/' . $materiel->getImage();
                    if (file_exists($oldImagePath)) {
                        unlink($oldImagePath);
                    }
                }

                $originalFilename = pathinfo($imageFile->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename.'-'.uniqid().'.'.$imageFile->guessExtension();

                try {
                    $imageFile->move(
                        $this->getParameter('uploads_directory'),
                        $newFilename
                    );
                    $materiel->setImage($newFilename);
                } catch (FileException $e) {
                    $this->addFlash('error', 'Échec du téléchargement de l\'image');
                }
            }

            $entityManager->flush();

            $this->addFlash('success', 'Matériel mis à jour avec succès!');
            return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('materiel/edit.html.twig', [
            'materiel' => $materiel,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_materiel_delete', methods: ['POST'])]
    public function delete(Request $request, Materiel $materiel, EntityManagerInterface $entityManager): Response
    {
        // Check if current user is the owner
        if ($materiel->getOwner() !== $this->getUser()) {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à supprimer ce matériel');
            return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
        }

        if ($this->isCsrfTokenValid('delete'.$materiel->getId(), $request->request->get('_token'))) {
            // Delete image file if exists
            if ($materiel->getImage()) {
                $imagePath = $this->getParameter('uploads_directory') . '/' . $materiel->getImage();
                if (file_exists($imagePath)) {
                    unlink($imagePath);
                }
            }

            $entityManager->remove($materiel);
            $entityManager->flush();
            $this->addFlash('success', 'Matériel supprimé avec succès!');
        }

        return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
    }

    #[Route('/{id}/toggle-visibility', name: 'app_materiel_toggle_visibility', methods: ['POST'])]
    public function toggleVisibility(Request $request, Materiel $materiel, EntityManagerInterface $entityManager): Response
    {
        // Check if user is owner or admin
        $user = $this->getUser();
        $isOwner = $materiel->getOwner() === $user;
        $isAdmin = in_array('ROLE_ADMIN', $user->getRoles());

        if (!$isOwner && !$isAdmin) {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à modifier la visibilité de ce matériel');
            return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
        }

        if ($this->isCsrfTokenValid('toggle'.$materiel->getId(), $request->request->get('_token'))) {
            $materiel->setVisible(!$materiel->isVisible());
            $entityManager->flush();

            $message = $materiel->isVisible() 
                ? 'Le matériel est maintenant visible par la communauté' 
                : 'Le matériel a été masqué de la communauté';
            
            $this->addFlash('success', $message);
        }

        return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
    }
}