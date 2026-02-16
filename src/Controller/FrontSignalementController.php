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
use Symfony\Component\String\Slugger\SluggerInterface;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Dompdf\Dompdf;
use Dompdf\Options;
#[Route('/signalement')]
final class FrontSignalementController extends AbstractController
{
    /**
     * PAGE D'ACCUEIL : Interface fluide avec choix Créer ou Suivre
     */
    #[Route('/accueil', name: 'app_signalement_home', methods: ['GET'])]
    public function home(): Response
    {
        return $this->render('front_signalement/home.html.twig');
    }

    /**
     * RECHERCHE : Reçoit l'ID du formulaire et redirige vers la vue détaillée
     */
    #[Route('/recherche', name: 'app_signalement_search', methods: ['POST'])]
    public function search(Request $request): Response
    {
        $id = $request->request->get('id_signalement');
        
        if (!$id) {
            $this->addFlash('danger', 'Veuillez entrer un numéro de suivi valide.');
            return $this->redirectToRoute('app_signalement_home');
        }

        return $this->redirectToRoute('app_front_signalement_show', ['id_signalement' => $id]);
    }

    /**
     * CRÉATION : Le client remplit le formulaire avec upload d'image
     */
   #[Route('/nouveau', name: 'app_front_signalement_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager, SluggerInterface $slugger): Response
    {
        $signalement = new Signalement();
        
        // 1. Valeurs par défaut automatiques
        $signalement->setDateCreation(new \DateTime());
        $signalement->setStatutTraitement('En attente');
        $signalement->setEstArchive(false);
        $signalement->setIdUser(15);
        // 2. Attribution de l'utilisateur par défaut (ID 15)
        // Note : On suppose que ton entité User existe avec l'ID 15
       // $userRepo = $entityManager->getRepository(\App\Entity\User::class);
       // $defaultUser = $userRepo->find(15);
       //// if ($defaultUser) {
      //      $signalement->setIdUser($defaultUser);
      //  }

        // 3. Attribution de la catégorie par défaut "Non classé"
        $catRepo = $entityManager->getRepository(\App\Entity\Categorie::class);
        $defaultCat = $catRepo->findOneBy(['nom_categorie' => 'Non classé']);
        if ($defaultCat) {
            $signalement->setCategorie($defaultCat);
        }

        $form = $this->createForm(SignalementType::class, $signalement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            /** @var UploadedFile $imageFile */
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
                    $signalement->setImageUrl($newFilename);
                } catch (FileException $e) {
                    $this->addFlash('danger', "Une erreur est survenue lors de l'upload de l'image.");
                }
            }

            $entityManager->persist($signalement);
            $entityManager->flush();

            $this->addFlash('success', 'Votre signalement est enregistré ! Numéro de suivi : ' . $signalement->getIdSignalement());
            
            return $this->redirectToRoute('app_front_signalement_show', [
                'id_signalement' => $signalement->getIdSignalement()
            ]);
        }

        return $this->render('front_signalement/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    /**
     * CONSULTATION : Affiche les détails d'UN SEUL signalement via son ID
     */
    #[Route('/consulter/{id_signalement}', name: 'app_front_signalement_show', methods: ['GET'])]
    public function show(
        #[MapEntity(mapping: ['id_signalement' => 'id_signalement'])] Signalement $signalement
    ): Response {
        if ($signalement->isEstArchive()) {
            throw $this->createNotFoundException('Ce signalement n\'est plus disponible.');
        }

        return $this->render('front_signalement/show.html.twig', [
            'signalement' => $signalement,
        ]);
    }

    /**
     * ARCHIVAGE : Le client décide de ne plus suivre ce ticket
     */
    #[Route('/{id_signalement}/archive', name: 'app_front_signalement_delete', methods: ['POST'])]
    public function archive(
        Request $request, 
        #[MapEntity(mapping: ['id_signalement' => 'id_signalement'])] Signalement $signalement, 
        EntityManagerInterface $entityManager
    ): Response {
        if ($this->isCsrfTokenValid('archive'.$signalement->getIdSignalement(), $request->request->get('_token'))) {
            $signalement->setEstArchive(true);
            $entityManager->flush();
            
            $this->addFlash('success', 'Le suivi de votre signalement a été arrêté.');
        }

        return $this->redirectToRoute('app_signalement_home');
    }
    #[Route('/{id_signalement}/pdf', name: 'app_front_signalement_pdf', methods: ['GET'])]
public function generatePdf(
    #[MapEntity(mapping: ['id_signalement' => 'id_signalement'])] Signalement $signalement): Response {
    $pdfOptions = new Options();
    $pdfOptions->set('defaultFont', 'Arial');
    $pdfOptions->set('isRemoteEnabled', true); 
    $pdfOptions->set('chroot', $this->getParameter('kernel.project_dir') . '/public');

    $dompdf = new Dompdf($pdfOptions);

    // ICI : On récupère le chemin racine et on le passe au template
    $html = $this->renderView('front_signalement/pdf.html.twig', [
        'signalement' => $signalement,
        'projectDir' => $this->getParameter('kernel.project_dir') // On l'appelle projectDir
    ]);

    $dompdf->loadHtml($html);
    $dompdf->setPaper('A4', 'portrait');
    $dompdf->render();

    return new Response($dompdf->output(), 200, [
        'Content-Type' => 'application/pdf',
        'Content-Disposition' => 'inline; filename="ticket-greencore-'.$signalement->getIdSignalement().'.pdf"'
    ]);
}
}