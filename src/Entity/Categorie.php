<?php

namespace App\Entity;

use App\Repository\CategorieRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
// 1. On n'oublie pas l'import du Validator
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: CategorieRepository::class)]
class Categorie
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "id_categorie")]
    private ?int $id_categorie = null;

    #[ORM\Column(length: 50)]
    #[Assert\NotBlank(message: "Le nom de la catégorie est obligatoire.")]
    #[Assert\Length(
        min: 3, 
        minMessage: "Le nom doit faire au moins {{ limit }} caractères."
    )]
    private ?string $nom_categorie = null;

    #[ORM\Column(type: Types::TEXT, nullable: true)]
    // Pas de NotBlank ici, car l'IA pourra le remplir plus tard
    private ?string $instruction_tri = null;

    #[ORM\Column(nullable: true)]
    #[Assert\Positive(message: "Le score de priorité doit être un chiffre positif.")]
    // Note : C'est dans le Formulaire (CRUD) qu'on transformera ça en liste déroulante 1, 2, 3...
    private ?int $score_priorite = null;

    #[ORM\Column(length: 100, nullable: true)]
    // Pas de contrainte ici non plus
    private ?string $temps_decomposition = null;

    // --- GETTERS ET SETTERS ---

    public function getIdCategorie(): ?int
    {
        return $this->id_categorie;
    }

    public function getNomCategorie(): ?string
    {
        return $this->nom_categorie;
    }

    public function setNomCategorie(string $nom_categorie): static
    {
        $this->nom_categorie = $nom_categorie;
        return $this;
    }

    public function getInstructionTri(): ?string
    {
        return $this->instruction_tri;
    }

    public function setInstructionTri(?string $instruction_tri): static
    {
        $this->instruction_tri = $instruction_tri;
        return $this;
    }

    public function getScorePriorite(): ?int
    {
        return $this->score_priorite;
    }

    public function setScorePriorite(?int $score_priorite): static
    {
        $this->score_priorite = $score_priorite;
        return $this;
    }

    public function getTempsDecomposition(): ?string
    {
        return $this->temps_decomposition;
    }

    public function setTempsDecomposition(?string $temps_decomposition): static
    {
        $this->temps_decomposition = $temps_decomposition;
        return $this;
    }
    
    // Indispensable pour la liste déroulante dans le formulaire Signalement
    public function __toString(): string
    {
        return $this->nom_categorie;
    }
}