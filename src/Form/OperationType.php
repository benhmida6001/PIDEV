<?php

namespace App\Form;

use App\Entity\Materiel;
use App\Entity\Operation;
use App\Entity\User;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class OperationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('type', ChoiceType::class, [
            'choices' => [
                'Borrow' => 'borrow',
                'Repair' => 'repair',
            ]
        ])
        ->add('status', ChoiceType::class, [
            'choices' => [
                'Pending' => 'pending',
                'Accepted' => 'accepted',
                'Refused' => 'refused',
                'Completed' => 'completed',
            ]
        ])
            ->add('startDate')
            ->add('endDate')
            ->add('returnImage')
            ->add('material', EntityType::class, [
                'class' => Materiel::class,
                'choice_label' => 'id',
            ])
            ->add('user', EntityType::class, [
                'class' => User::class,
                'choice_label' => 'id',
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Operation::class,
        ]);
    }
}
