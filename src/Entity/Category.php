<?php

namespace App\Entity;

use App\Repository\CategoryRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CategoryRepository::class)]
class Category
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $name = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(string $name): static
    {
        $this->name = $name;

        return $this;
    }

    public function getNameInFrench(): string
{
    $translations = [
        'Camping Equipment' => 'Équipement de Camping',
        'Cleaning Tools' => 'Outils de Nettoyage',
        'Recycling Equipment' => 'Équipement de Recyclage',
        'Event Equipment' => 'Équipement d\'Événement',
        'Transportation' => 'Transport',
        'Safety Equipment' => 'Équipement de Sécurité',
        'Communication' => 'Communication',
        'Other' => 'Autre',
    ];

    return $translations[$this->name] ?? $this->name;
}
}
