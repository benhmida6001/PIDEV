<?php
// filepath: /c:/Users/bahae/Documents/BenH/PIDEV/src/Controller/OperationController.php

namespace App\Controller;

use App\Entity\Operation;
use App\Form\OperationType;
use App\Repository\OperationRepository;
use App\Repository\MaterielRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/operation')]
class OperationController extends AbstractController
{
    #[Route('/', name: 'app_operation_index', methods: ['GET'])]
    public function index(OperationRepository $operationRepository): Response
    {
        $currentUser = $this->getUser();
        
        // Get all operations for the current user
        $operations = $operationRepository->createQueryBuilder('o')
            ->where('o.requester = :user')
            ->setParameter('user', $currentUser)
            ->orderBy('o.createdAt', 'DESC')
            ->getQuery()
            ->getResult();

        return $this->render('operation/index.html.twig', [
            'operations' => $operations,
        ]);
    }

    #[Route('/new', name: 'app_operation_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager, MaterielRepository $materielRepository): Response
    {
        // Get and validate type
        $type = $request->query->get('type');
        if (!$type || !in_array($type, ['borrow', 'repair'])) {
            $this->addFlash('error', 'Type d\'opération invalide');
            return $this->redirectToRoute('app_materiel_index');
        }

        $operation = new Operation();
        $operation->setType($type);
        
        // Pre-select material if provided in URL
        $materielId = $request->query->get('materiel');
        if ($materielId) {
            $materiel = $materielRepository->find($materielId);
            if ($materiel && $materiel->isVisible() && $materiel->getAvailableQuantity() > 0) {
                $operation->setMateriel($materiel);
            } else {
                $this->addFlash('error', 'Ce matériel n\'est pas disponible');
                return $this->redirectToRoute('app_materiel_index');
            }
        }
        
        $form = $this->createForm(OperationType::class, $operation);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Validate dates
            if ($operation->getEndDate() <= $operation->getStartDate()) {
                $this->addFlash('error', 'La date de fin doit être après la date de début');
                return $this->render('operation/new.html.twig', [
                    'operation' => $operation,
                    'form' => $form,
                    'preselectedMateriel' => $materielId ? true : false,
                    'operationType' => $type,
                ]);
            }

            // Validate quantity
            if ($operation->getQuantity() > $operation->getMateriel()->getAvailableQuantity()) {
                $this->addFlash('error', 'La quantité demandée dépasse la quantité disponible');
                return $this->render('operation/new.html.twig', [
                    'operation' => $operation,
                    'form' => $form,
                    'preselectedMateriel' => $materielId ? true : false,
                    'operationType' => $type,
                ]);
            }

            $operation->setRequester($this->getUser());
            $operation->setStatus('pending');
            $operation->setCreatedAt(new \DateTimeImmutable());

            $entityManager->persist($operation);
            $entityManager->flush();

            $this->addFlash('success', 'Votre demande a été envoyée avec succès!');
            return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('operation/new.html.twig', [
            'operation' => $operation,
            'form' => $form,
            'preselectedMateriel' => $materielId ? true : false,
            'operationType' => $type,
        ]);
    }

    #[Route('/{id}', name: 'app_operation_show', methods: ['GET'])]
    public function show(Operation $operation): Response
    {
        return $this->render('operation/show.html.twig', [
            'operation' => $operation,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_operation_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Operation $operation, EntityManagerInterface $entityManager): Response
    {
        // Only allow editing pending operations
        if ($operation->getStatus() !== 'pending') {
            $this->addFlash('error', 'Vous ne pouvez modifier que les demandes en attente');
            return $this->redirectToRoute('app_operation_index', [], Response::HTTP_SEE_OTHER);
        }

        // Only allow the requester to edit
        if ($operation->getRequester() !== $this->getUser()) {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à modifier cette demande');
            return $this->redirectToRoute('app_operation_index', [], Response::HTTP_SEE_OTHER);
        }

        $form = $this->createForm(OperationType::class, $operation);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            $this->addFlash('success', 'Demande mise à jour avec succès');
            return $this->redirectToRoute('app_operation_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('operation/edit.html.twig', [
            'operation' => $operation,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_operation_delete', methods: ['POST'])]
    public function delete(Request $request, Operation $operation, EntityManagerInterface $entityManager): Response
    {
        if ($operation->getRequester() !== $this->getUser()) {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à supprimer cette demande');
            return $this->redirectToRoute('app_operation_index', [], Response::HTTP_SEE_OTHER);
        }

        if ($this->isCsrfTokenValid('delete'.$operation->getId(), $request->request->get('_token'))) {
            $entityManager->remove($operation);
            $entityManager->flush();
            $this->addFlash('success', 'Demande supprimée avec succès');
        }

        return $this->redirectToRoute('app_operation_index', [], Response::HTTP_SEE_OTHER);
    }

    #[Route('/{id}/approve', name: 'app_operation_approve', methods: ['POST'])]
    public function approve(Request $request, Operation $operation, EntityManagerInterface $entityManager): Response
    {
        if ($operation->getMateriel()->getOwner() !== $this->getUser()) {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à approuver cette demande');
            return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
        }

        if ($this->isCsrfTokenValid('approve'.$operation->getId(), $request->request->get('_token'))) {
            $operation->setStatus('approved');
            
            $materiel = $operation->getMateriel();
            $newAvailableQuantity = $materiel->getAvailableQuantity() - $operation->getQuantity();
            $materiel->setAvailableQuantity(max(0, $newAvailableQuantity));
            
            $entityManager->flush();
            
            $this->addFlash('success', 'Demande acceptée avec succès!');
        }

        return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
    }

    #[Route('/{id}/reject', name: 'app_operation_reject', methods: ['POST'])]
    public function reject(Request $request, Operation $operation, EntityManagerInterface $entityManager): Response
    {
        if ($operation->getMateriel()->getOwner() !== $this->getUser()) {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à refuser cette demande');
            return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
        }

        if ($this->isCsrfTokenValid('reject'.$operation->getId(), $request->request->get('_token'))) {
            $operation->setStatus('rejected');
            $entityManager->flush();
            
            $this->addFlash('success', 'Demande refusée');
        }

        return $this->redirectToRoute('app_materiel_index', [], Response::HTTP_SEE_OTHER);
    }
}