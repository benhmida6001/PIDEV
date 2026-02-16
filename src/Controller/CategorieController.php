<?php

namespace App\Controller;

use App\Entity\Categorie;
use App\Form\CategorieType;
use App\Repository\CategorieRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
// IMPORTANT : Cet import est nécessaire pour que ça marche avec id_categorie
use Symfony\Bridge\Doctrine\Attribute\MapEntity;

#[Route('/categorie')]
final class CategorieController extends AbstractController
{
    
    #[Route(name: 'app_categorie_index', methods: ['GET'])]
    public function index(CategorieRepository $categorieRepository, Request $request): Response
    {
        $search = $request->query->get('search', '');
        $sort = $request->query->get('sort', 'id_categorie'); 
        $dir = $request->query->get('dir', 'ASC');

        $dir = strtoupper($dir) === 'DESC' ? 'DESC' : 'ASC';

        // On utilise 'nom_categorie' !
        $allowedSorts = ['id_categorie', 'nom_categorie'];
        if (!in_array($sort, $allowedSorts)) {
            $sort = 'id_categorie';
        }

        $qb = $categorieRepository->createQueryBuilder('c');

        // Recherche sur 'nom_categorie' et 'instruction_tri'
        if ($search) {
            $qb->andWhere('c.nom_categorie LIKE :search OR c.instruction_tri LIKE :search')
               ->setParameter('search', '%' . $search . '%');
        }

        $qb->orderBy('c.' . $sort, $dir);

        return $this->render('categorie/index.html.twig', [
            'categories' => $qb->getQuery()->getResult(),
            'search' => $search,
            'sort' => $sort,
            'dir' => $dir
        ]);
    }
    #[Route('/new', name: 'app_categorie_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $categorie = new Categorie();
        $form = $this->createForm(CategorieType::class, $categorie);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($categorie);
            $entityManager->flush();

            return $this->redirectToRoute('app_categorie_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('categorie/new.html.twig', [
            'categorie' => $categorie,
            'form' => $form,
        ]);
    }

    // --- C'EST ICI QUE TOUT SE JOUE ---

    #[Route('/{id_categorie}', name: 'app_categorie_show', methods: ['GET'])]
    public function show(
        // On dit à Symfony : "L'URL contient 'id_categorie', fais le lien avec la colonne 'id_categorie' de la base"
        #[MapEntity(mapping: ['id_categorie' => 'id_categorie'])] 
        Categorie $categorie
    ): Response
    {
        return $this->render('categorie/show.html.twig', [
            'categorie' => $categorie,
        ]);
    }

    #[Route('/{id_categorie}/edit', name: 'app_categorie_edit', methods: ['GET', 'POST'])]
    public function edit(
        Request $request, 
        #[MapEntity(mapping: ['id_categorie' => 'id_categorie'])] 
        Categorie $categorie, 
        EntityManagerInterface $entityManager
    ): Response
    {
        $form = $this->createForm(CategorieType::class, $categorie);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_categorie_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('categorie/edit.html.twig', [
            'categorie' => $categorie,
            'form' => $form,
        ]);
    }

    #[Route('/{id_categorie}', name: 'app_categorie_delete', methods: ['POST'])]
    public function delete(
        Request $request, 
        #[MapEntity(mapping: ['id_categorie' => 'id_categorie'])] 
        Categorie $categorie, 
        EntityManagerInterface $entityManager
    ): Response
    {
        // On appelle la fonction telle qu'elle est écrite dans ton Entité (CamelCase)
        // Mais si tu tiens à getId_categorie(), PHP est parfois tolérant (case-insensitive).
        // Le plus sûr est d'utiliser le vrai nom de la méthode : getIdCategorie()
        if ($this->isCsrfTokenValid('delete'.$categorie->getIdCategorie(), $request->request->get('_token'))) {
            $entityManager->remove($categorie);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_categorie_index', [], Response::HTTP_SEE_OTHER);
    }
}