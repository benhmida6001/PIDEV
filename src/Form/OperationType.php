<?php
// filepath: /c:/Users/bahae/Documents/BenH/PIDEV/src/Form/OperationType.php

namespace App\Form;

use App\Entity\Materiel;
use App\Entity\Operation;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints as Assert;

class OperationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('materiel', EntityType::class, [
                'class' => Materiel::class,
                'choice_label' => 'name',
                'label' => 'Matériel',
                'placeholder' => 'Choisissez un matériel',
                'attr' => ['class' => 'form-select'],
                'query_builder' => function ($er) {
                    return $er->createQueryBuilder('m')
                        ->where('m.visible = true')
                        ->andWhere('m.availableQuantity > 0')
                        ->orderBy('m.name', 'ASC');
                },
            ])
            ->add('quantity', IntegerType::class, [
                'label' => 'Quantité',
                'attr' => [
                    'class' => 'form-control',
                    'min' => 1,
                    'placeholder' => 'Entrez la quantité',
                ],
                'constraints' => [
                    new Assert\NotBlank(['message' => 'La quantité est requise']),
                    new Assert\Positive(['message' => 'La quantité doit être positive']),
                ],
            ])
            ->add('startDate', DateType::class, [
                'label' => 'Date de début',
                'widget' => 'single_text',
                'attr' => [
                    'class' => 'form-control',
                    'min' => (new \DateTime())->format('Y-m-d'),
                ],
                'constraints' => [
                    new Assert\NotBlank(['message' => 'La date de début est requise']),
                    new Assert\GreaterThanOrEqual([
                        'value' => 'today',
                        'message' => 'La date de début doit être aujourd\'hui ou dans le futur',
                    ]),
                ],
            ])
            ->add('endDate', DateType::class, [
                'label' => 'Date de fin',
                'widget' => 'single_text',
                'attr' => [
                    'class' => 'form-control',
                    'min' => (new \DateTime())->format('Y-m-d'),
                ],
                'constraints' => [
                    new Assert\NotBlank(['message' => 'La date de fin est requise']),
                    new Assert\GreaterThan([
                        'propertyPath' => 'parent.all[startDate].data',
                        'message' => 'La date de fin doit être après la date de début',
                    ]),
                ],
            ])
            ->add('description', TextareaType::class, [
                'label' => 'Description / Raison',
                'required' => false,
                'attr' => [
                    'class' => 'form-control',
                    'rows' => 4,
                    'placeholder' => 'Décrivez votre demande...',
                ],
            ])
        ;
    }

    public function configureOptions(OptionsResolver $OptionsResolver): void
    {
        $OptionsResolver->setDefaults([
            'data_class' => Operation::class,
        ]);
    }
}