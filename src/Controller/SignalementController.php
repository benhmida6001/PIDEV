<?php

namespace App\Controller;

use App\Entity\Signalement;
use App\Form\SignalementType;
use App\Repository\SignalementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Bridge\Doctrine\Attribute\MapEntity;
// Ajout des imports nécessaires pour l'image
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\String\Slugger\SluggerInterface;

#[Route('/signalement')]
final class SignalementController extends AbstractController
{
   #[Route(name: 'app_signalement_index', methods: ['GET'])]
    public function index(SignalementRepository $signalementRepository, Request $request): Response
    {
        // 1. Récupération des paramètres (avec valeurs par défaut)
        $search = $request->query->get('search', '');
        $sort = $request->query->get('sort', 'id_signalement'); // On utilise le snake_case !
        $dir = $request->query->get('dir', 'ASC');

        // Sécurité pour la direction du tri
        $dir = strtoupper($dir) === 'DESC' ? 'DESC' : 'ASC';

        // Sécurité pour éviter qu'un utilisateur tape n'importe quoi dans l'URL
        $allowedSorts = ['id_signalement', 'description', 'localisation', 'date_creation', 'statut_traitement'];
        if (!in_array($sort, $allowedSorts)) {
            $sort = 'id_signalement';
        }

        // 2. Construction de la requête
        $qb = $signalementRepository->createQueryBuilder('s');

        // Recherche dynamique (sur la description, la localisation ou le statut)
        if ($search) {
            $qb->andWhere('s.description LIKE :search OR s.localisation LIKE :search OR s.statut_traitement LIKE :search')
               ->setParameter('search', '%' . $search . '%');
        }

        // Tri dynamique
        $qb->orderBy('s.' . $sort, $dir);

        // 3. Envoi à la vue
        return $this->render('signalement/index.html.twig', [
            'signalements' => $qb->getQuery()->getResult(),
            'search' => $search,
            'sort' => $sort,
            'dir' => $dir
        ]);
    }

    #[Route('/new', name: 'app_signalement_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager, SluggerInterface $slugger): Response
    {
        $signalement = new Signalement();
        // Si tu as besoin d'initialiser la date manuellement, décommente :
        // $signalement->setDateCreation(new \DateTimeImmutable());
        
        $form = $this->createForm(SignalementType::class, $signalement);
        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            if ($form->isValid()) {
                
                // --- DEBUT LOGIQUE IMAGE ---
                $imageFile = $form->get('imageFile')->getData();

                if ($imageFile) {
                    $originalFilename = pathinfo($imageFile->getClientOriginalName(), PATHINFO_FILENAME);
                    // Nettoyage du nom de fichier
                    $safeFilename = $slugger->slug($originalFilename);
                    $newFilename = $safeFilename.'-'.uniqid().'.'.$imageFile->guessExtension();

                    try {
                       $imageFile->move(
    $this->getParameter('kernel.project_dir') . '/public/assets_sc/uploads',
    $newFilename
);
                    } catch (FileException $e) {
                        // Gérer l'erreur si besoin
                    }

                    // On met à jour l'entité avec le nom du fichier
                    $signalement->setImageUrl($newFilename);
                }
                // --- FIN LOGIQUE IMAGE ---

                $entityManager->persist($signalement);
                $entityManager->flush();

                return $this->redirectToRoute('app_signalement_index', [], Response::HTTP_SEE_OTHER);
            } else {
                // --- DEBUG ---
                // dd($form->getErrors(true)); 
            }
        }

        return $this->render('signalement/new.html.twig', [
            'signalement' => $signalement,
            'form' => $form,
        ]);
    }

    #[Route('/{id_signalement}', name: 'app_signalement_show', methods: ['GET'])]
    public function show(
        #[MapEntity(mapping: ['id_signalement' => 'id_signalement'])] 
        Signalement $signalement
    ): Response
    {
        return $this->render('signalement/show.html.twig', [
            'signalement' => $signalement,
        ]);
    }

    #[Route('/{id_signalement}/edit', name: 'app_signalement_edit', methods: ['GET', 'POST'])]
    public function edit(
        Request $request, 
        #[MapEntity(mapping: ['id_signalement' => 'id_signalement'])] 
        Signalement $signalement, 
        EntityManagerInterface $entityManager,
        SluggerInterface $slugger
    ): Response
    {
        $form = $this->createForm(SignalementType::class, $signalement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            
            // --- DEBUT LOGIQUE IMAGE (EDIT) ---
            $imageFile = $form->get('imageFile')->getData();

            if ($imageFile) {
                $originalFilename = pathinfo($imageFile->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename.'-'.uniqid().'.'.$imageFile->guessExtension();

                try {
                   $imageFile->move(
                     $this->getParameter('kernel.project_dir') . '/public/assets_sc/uploads',
                     $newFilename
                    );
                } catch (FileException $e) {
                    // Erreur upload
                }

                $signalement->setImageUrl($newFilename);
            }
            // --- FIN LOGIQUE IMAGE ---

            $entityManager->flush();

            return $this->redirectToRoute('app_signalement_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('signalement/edit.html.twig', [
            'signalement' => $signalement,
            'form' => $form,
        ]);
    }

    #[Route('/{id_signalement}', name: 'app_signalement_delete', methods: ['POST'])]
    public function delete(
        Request $request, 
        #[MapEntity(mapping: ['id_signalement' => 'id_signalement'])] 
        Signalement $signalement, 
        EntityManagerInterface $entityManager
    ): Response
    {
        // On suppose que le getter est getIdSignalement()
        if ($this->isCsrfTokenValid('delete'.$signalement->getIdSignalement(), $request->getPayload()->getString('_token'))) {
            $entityManager->remove($signalement);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_signalement_index', [], Response::HTTP_SEE_OTHER);
    }
}