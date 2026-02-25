<?php

namespace App\Form;

use App\Entity\UserPreferences;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;

class UserPreferencesType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('language', ChoiceType::class, [
                'choices' => [
                    'Français' => 'fr',
                    'English' => 'en',
                    'Español' => 'es',
                    'Deutsch' => 'de',
                    'Italiano' => 'it',
                    'Português' => 'pt',
                    'Nederlands' => 'nl',
                    'Русский' => 'ru',
                    '中文' => 'zh',
                    '日本語' => 'ja',
                ],
                'label' => 'preferences.language',
                'attr' => ['class' => 'form-control']
            ])
            ->add('theme', ChoiceType::class, [
                'choices' => [
                    'preferences.default' => 'default',
                    'preferences.dark' => 'dark',
                    'preferences.light' => 'light',
                ],
                'label' => 'preferences.theme',
                'attr' => ['class' => 'form-control']
            ])
            ->add('notifications', CheckboxType::class, [
                'label' => 'preferences.notifications',
                'required' => false,
                'attr' => ['class' => 'form-check-input']
            ])
            ->add('emailNotifications', CheckboxType::class, [
                'label' => 'preferences.email_notifications',
                'required' => false,
                'attr' => ['class' => 'form-check-input']
            ])
            ->add('soundEffects', CheckboxType::class, [
                'label' => 'preferences.sound_effects',
                'required' => false,
                'attr' => ['class' => 'form-check-input']
            ])
            ->add('itemsPerPage', IntegerType::class, [
                'label' => 'preferences.items_per_page',
                'attr' => [
                    'class' => 'form-control',
                    'min' => 5,
                    'max' => 50
                ]
            ])
            ->add('dateFormat', ChoiceType::class, [
                'choices' => [
                    'preferences.format_dmy' => 'd/m/Y',
                    'preferences.format_mdy' => 'm/d/Y',
                    'preferences.format_ymd' => 'Y/m/d',
                    'preferences.format_dmy_long' => 'd F Y',
                ],
                'label' => 'preferences.date_format',
                'attr' => ['class' => 'form-control']
            ])
            ->add('timeFormat', ChoiceType::class, [
                'choices' => [
                    'preferences.format_24h' => '24h',
                    'preferences.format_12h' => '12h',
                ],
                'label' => 'preferences.time_format',
                'attr' => ['class' => 'form-control']
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => UserPreferences::class,
        ]);
    }
}
