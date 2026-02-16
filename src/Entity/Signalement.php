<?php

namespace App\Entity;

use App\Repository\SignalementRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
// Importation du validateur (Workshop page 4)
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: SignalementRepository::class)]
#[ORM\Index(name: 'fk_sig_cat', columns: ['id_categorie'])] // On garde ton index pour éviter l'erreur SQL
class Signalement
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "id_signalement")] 
    private ?int $id_signalement = null;

    #[ORM\Column(length: 255, nullable: true)]
    //#[Assert\NotBlank(message: "Veuillez joindre une image du signalement.")]
    private ?string $image_url = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Assert\NotBlank(message: "La description ne peut pas être vide.")]
    #[Assert\Length(
        min: 10, 
        minMessage: "La description doit faire au moins {{ limit }} caractères pour être détaillée."
    )]
    private ?string $description = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "La localisation est obligatoire (GPS ou adresse).")]
    private ?string $localisation = null;

    #[ORM\Column(type: Types::DATETIME_MUTABLE)]
    #[Assert\Type("\DateTimeInterface")]
    private ?\DateTimeInterface $date_creation = null;

    #[ORM\Column(length: 35)]
    private ?string $statut_traitement = "En attente"; // Valeur par défaut logique

    #[ORM\Column(type: Types::DATETIME_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $date_traitement = null;

    #[ORM\Column(nullable: true)]
    private ?int $id_user = null;

    #[ORM\ManyToOne(targetEntity: Categorie::class)]
    #[ORM\JoinColumn(name: "id_categorie", referencedColumnName: "id_categorie", nullable: false)]
    #[Assert\NotBlank(message: "Veuillez sélectionner une catégorie.")]
    private ?Categorie $categorie = null;

    #[ORM\Column]
    private ?bool $est_archive = false;

    // CONSTRUCTEUR : Pour initialiser la date système par défaut
    public function __construct()
    {
        $this->date_creation = new \DateTime(); // Date actuelle par défaut
        $this->statut_traitement = "En attente";
    }

    // GETTERS ET SETTERS 

    public function getIdSignalement(): ?int
    {
        return $this->id_signalement;
    }

    public function getImageUrl(): ?string
    {
        return $this->image_url;
    }

    public function setImageUrl(?string $image_url): static
    {
        $this->image_url = $image_url;
        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): static
    {
        $this->description = $description;
        return $this;
    }

    public function getLocalisation(): ?string
    {
        return $this->localisation;
    }

    public function setLocalisation(string $localisation): static
    {
        $this->localisation = $localisation;
        return $this;
    }

    public function getDateCreation(): ?\DateTimeInterface
    {
        return $this->date_creation;
    }

    public function setDateCreation(\DateTimeInterface $date_creation): static
    {
        $this->date_creation = $date_creation;
        return $this;
    }

    public function getStatutTraitement(): ?string
    {
        return $this->statut_traitement;
    }

    public function setStatutTraitement(string $statut_traitement): static
    {
        $this->statut_traitement = $statut_traitement;
        return $this;
    }

    public function getDateTraitement(): ?\DateTimeInterface
    {
        return $this->date_traitement;
    }

    public function setDateTraitement(?\DateTimeInterface $date_traitement): static
    {
        $this->date_traitement = $date_traitement;
        return $this;
    }

    public function getIdUser(): ?int
    {
        return $this->id_user;
    }

    public function setIdUser(?int $id_user): static
    {
        $this->id_user = $id_user;
        return $this;
    }

    public function getCategorie(): ?Categorie
    {
        return $this->categorie;
    }

    public function setCategorie(?Categorie $categorie): static
    {
        $this->categorie = $categorie;
        return $this;
    }

    public function isEstArchive(): ?bool
    {
        return $this->est_archive;
    }

    public function setEstArchive(bool $est_archive): static
    {
        $this->est_archive = $est_archive;

        return $this;
    }
}