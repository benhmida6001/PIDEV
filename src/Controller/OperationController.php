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
use App\Service\ImageComparisonService;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\JsonResponse;


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


    #[Route('/operation/{id}/return', name: 'app_operation_return', methods: ['POST'])]
public function returnMaterial(
    Operation $operation,
    Request $request,
    EntityManagerInterface $entityManager,
    ImageComparisonService $imageComparisonService
): Response {
    $validStatuses = ['approved', 'accepted'];
    
    if (!in_array($operation->getStatus(), $validStatuses) || $operation->getReturnedAt() !== null) {
        $this->addFlash('error', 'Cette opération ne peut pas être retournée. Status: ' . $operation->getStatus());
        return $this->redirectToRoute('app_materiel_index');
    }

    $returnedImageFile = $request->files->get('returned_image');
    
    if (!$returnedImageFile) {
        $this->addFlash('error', 'Veuillez fournir une image de retour.');
        return $this->redirectToRoute('app_materiel_index');
    }

    $projectDir = $this->getParameter('kernel.project_dir');
    $uploadDir = $projectDir . DIRECTORY_SEPARATOR . 'public' . DIRECTORY_SEPARATOR . 'uploads' . DIRECTORY_SEPARATOR . 'materiels';
    
    if (!file_exists($uploadDir)) {
        mkdir($uploadDir, 0777, true);
    }
    
    $newFilename = uniqid() . '.' . $returnedImageFile->guessExtension();
    
    try {
        $returnedImageFile->move($uploadDir, $newFilename);
    } catch (FileException $e) {
        $this->addFlash('error', 'Erreur lors du téléchargement: ' . $e->getMessage());
        return $this->redirectToRoute('app_materiel_index');
    }

    $materiel = $operation->getMateriel();
    $materielImage = $materiel->getImage();
    
    // Build proper paths
    $originalImagePath = $projectDir . DIRECTORY_SEPARATOR . 'public' . DIRECTORY_SEPARATOR . 'uploads' . DIRECTORY_SEPARATOR . 'materiels' . DIRECTORY_SEPARATOR . $materielImage;
    $returnedImagePath = $uploadDir . DIRECTORY_SEPARATOR . $newFilename;

    // Debug: log paths
    error_log("Original path: " . $originalImagePath);
    error_log("Returned path: " . $returnedImagePath);
    error_log("Original exists: " . (file_exists($originalImagePath) ? 'YES' : 'NO'));
    error_log("Returned exists: " . (file_exists($returnedImagePath) ? 'YES' : 'NO'));

    if (!file_exists($originalImagePath)) {
        $this->addFlash('error', 'Image originale introuvable: ' . $materielImage);
        return $this->redirectToRoute('app_materiel_index');
    }

    try {
        $comparisonResult = $imageComparisonService->compareImages(
            $originalImagePath,
            $returnedImagePath,
            $materiel->getStatus()
        );
    } catch (\Exception $e) {
        $this->addFlash('error', 'Erreur AI: ' . $e->getMessage());
        return $this->redirectToRoute('app_materiel_index');
    }

    $operation->setReturnedImage($newFilename);
    $operation->setVerificationResult($comparisonResult['explanation']);
    $operation->setReturnedAt(new \DateTimeImmutable());
    $operation->setStatus('returned');

    $materiel->setStatus($comparisonResult['status']);
    $materiel->setAvailableQuantity($materiel->getAvailableQuantity() + $operation->getQuantity());
    $materiel->setImage($newFilename);

    $entityManager->flush();

    $this->addFlash('success', 'Matériel retourné! Nouvel état: ' . $comparisonResult['status']);

    return $this->redirectToRoute('app_materiel_index');
}

#[Route('/operation/{id}/return-confirm', name: 'app_operation_return_confirm', methods: ['POST'])]
public function returnConfirm(Operation $operation, Request $request): Response
{
    $validStatuses = ['approved', 'accepted'];
    
    if (!in_array($operation->getStatus(), $validStatuses)) {
        $this->addFlash('error', 'Cette opération ne peut pas être retournée.');
        return $this->redirectToRoute('app_materiel_index');
    }

    return $this->render('operation/return_confirm.html.twig', [
        'operation' => $operation,
    ]);
}

#[Route('/operation/{id}/analyze-return', name: 'app_operation_analyze_return', methods: ['POST'])]
public function analyzeReturn(
    Operation $operation,
    Request $request,
    ImageComparisonService $imageComparisonService
): JsonResponse {
    $returnedImageData = $request->request->get('returned_image_data');
    
    if (!$returnedImageData) {
        return new JsonResponse(['error' => 'Image manquante'], 400);
    }

    $imageData = explode(',', $returnedImageData)[1];
    $decodedImage = base64_decode($imageData);
    
    $projectDir = $this->getParameter('kernel.project_dir');
    $uploadDir = $projectDir . DIRECTORY_SEPARATOR . 'public' . DIRECTORY_SEPARATOR . 'uploads' . DIRECTORY_SEPARATOR . 'materiels';
    
    if (!file_exists($uploadDir)) {
        mkdir($uploadDir, 0777, true);
    }
    
    $newFilename = uniqid() . '.png';
    file_put_contents($uploadDir . DIRECTORY_SEPARATOR . $newFilename, $decodedImage);
    
    $materiel = $operation->getMateriel();
    $originalImagePath = $uploadDir . DIRECTORY_SEPARATOR . $materiel->getImage();
    $returnedImagePath = $uploadDir . DIRECTORY_SEPARATOR . $newFilename;

    try {
        $result = $imageComparisonService->compareImages(
            $originalImagePath,
            $returnedImagePath,
            $materiel->getStatus()
        );
        
        $request->getSession()->set('return_temp_image_' . $operation->getId(), $newFilename);
        $request->getSession()->set('return_result_' . $operation->getId(), $result);
        
        return new JsonResponse($result);
    } catch (\Exception $e) {
        return new JsonResponse(['error' => $e->getMessage()], 500);
    }
}

#[Route('/operation/{id}/process-return', name: 'app_operation_process_return', methods: ['POST'])]

public function processReturn(
    Operation $operation,
    Request $request,
    EntityManagerInterface $entityManager
): Response {
    if ($request->request->get('confirm_return') !== '1') {
        $this->addFlash('error', 'Retour non confirmé.');
        return $this->redirectToRoute('app_materiel_index');
    }

    $newFilename = $request->getSession()->get('return_temp_image_' . $operation->getId());
    $result = $request->getSession()->get('return_result_' . $operation->getId());
    
    if (!$newFilename || !$result) {
        $this->addFlash('error', 'Session expirée. Veuillez recommencer.');
        return $this->redirectToRoute('app_materiel_index');
    }

    $materiel = $operation->getMateriel();
    $oldQuantity = $materiel->getAvailableQuantity();
    
    // No need to copy - image is already in materiels folder
    $operation->setReturnedImage($newFilename);
    $operation->setVerificationResult($result['explanation']);
    $operation->setReturnedAt(new \DateTimeImmutable());
    $operation->setStatus('returned');

    $materiel->setStatus($result['status']);
    $newQuantity = $oldQuantity + $operation->getQuantity();
    $materiel->setAvailableQuantity($newQuantity);
    $materiel->setImage($newFilename);

    $entityManager->flush();
    
    $request->getSession()->remove('return_temp_image_' . $operation->getId());
    $request->getSession()->remove('return_result_' . $operation->getId());

    $this->addFlash('success', sprintf(
        'Matériel retourné! État: %s. Quantité: %d → %d (+%d)',
        $result['status'],
        $oldQuantity,
        $newQuantity,
        $operation->getQuantity()
    ));

    return $this->redirectToRoute('app_materiel_index');
}}