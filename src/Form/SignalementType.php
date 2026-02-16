<?php

namespace App\Form;

use App\Entity\Categorie;
use App\Entity\Signalement;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Validator\Constraints\NotBlank; // <-- AJOUT OBLIGATOIRE

class SignalementType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            // 1. Catégorie
            ->add('categorie', EntityType::class, [
                'class' => Categorie::class,
                'choice_label' => 'nom_categorie',
                'placeholder' => 'Choisir une catégorie',
                'label' => 'Catégorie',
                'attr' => ['class' => 'form-select mb-3']
            ])

            // 2. Statut
            ->add('statut_traitement', ChoiceType::class, [
                'choices'  => [
                    'En attente' => 'En attente',
                    'En cours de traitement' => 'En cours de traitement',
                    'Traité' => 'Traité',
                ],
                'label' => 'Statut du signalement',
                'attr' => ['class' => 'form-select mb-3']
            ])

            // 3. Image (CORRIGÉ ICI)
            ->add('imageFile', FileType::class, [
                'label' => 'Preuve (Image du problème)',
                'mapped' => false, 
                'required' => true, // On met TRUE pour l'affichage (l'étoile *)
                'attr' => ['class' => 'form-control mb-3'],
                'constraints' => [
                    // On oblige l'utilisateur à mettre une image ICI
                    new NotBlank([
                        'message' => 'Veuillez joindre une image du signalement.',
                    ]),
                    new File([
                        'maxSize' => '5M',
                        'mimeTypes' => [
                            'image/jpeg',
                            'image/png',
                            'image/webp',
                        ],
                        'mimeTypesMessage' => 'Veuillez uploader une image valide (JPG, PNG)',
                    ])
                ],
            ])

            // 4. Description
            ->add('description', TextareaType::class, [
                'label' => 'Description détaillée',
                'attr' => ['class' => 'form-control mb-3', 'rows' => 5, 'placeholder' => 'Décrivez le problème...']
            ])

            // Localisation
            ->add('localisation', TextType::class, [
                'label' => 'Localisation (Adresse ou Lieu)',
                'attr' => ['class' => 'form-control mb-3']
            ])

            // 5. Dates
            ->add('date_creation', DateType::class, [
                'widget' => 'single_text',
                 'required' => false,

                'label' => 'Date du signalement',
                'attr' => ['class' => 'form-control mb-3']
            ])
            
            ->add('date_traitement', DateType::class, [
                'widget' => 'single_text',
                'required' => false,
                'label' => 'Date de résolution (si traité)',
                'attr' => ['class' => 'form-control mb-3']
            ])

            // 6. User
            ->add('id_user', null, [
                'label' => 'ID Utilisateur',
                'attr' => ['class' => 'form-control mb-3']
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Signalement::class,
        ]);
    }
}