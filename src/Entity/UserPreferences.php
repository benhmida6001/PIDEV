<?php

namespace App\Entity;

use App\Repository\UserPreferencesRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: UserPreferencesRepository::class)]
class UserPreferences
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 10)]
    private ?string $language = 'fr';

    #[ORM\Column(length: 20)]
    private ?string $theme = 'default';

    #[ORM\Column]
    private ?bool $notifications = true;

    #[ORM\Column]
    private ?bool $emailNotifications = true;

    #[ORM\Column]
    private ?bool $soundEffects = false;

    #[ORM\Column]
    private ?int $itemsPerPage = 10;

    #[ORM\Column(length: 20)]
    private ?string $dateFormat = 'd/m/Y';

    #[ORM\Column(length: 10)]
    private ?string $timeFormat = '24h';

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getLanguage(): ?string
    {
        return $this->language;
    }

    public function setLanguage(string $language): static
    {
        $this->language = $language;

        return $this;
    }

    public function getTheme(): ?string
    {
        return $this->theme;
    }

    public function setTheme(string $theme): static
    {
        $this->theme = $theme;

        return $this;
    }

    public function isNotifications(): ?bool
    {
        return $this->notifications;
    }

    public function setNotifications(bool $notifications): static
    {
        $this->notifications = $notifications;

        return $this;
    }

    public function isEmailNotifications(): ?bool
    {
        return $this->emailNotifications;
    }

    public function setEmailNotifications(bool $emailNotifications): static
    {
        $this->emailNotifications = $emailNotifications;

        return $this;
    }

    public function isSoundEffects(): ?bool
    {
        return $this->soundEffects;
    }

    public function setSoundEffects(bool $soundEffects): static
    {
        $this->soundEffects = $soundEffects;

        return $this;
    }

    public function getItemsPerPage(): ?int
    {
        return $this->itemsPerPage;
    }

    public function setItemsPerPage(int $itemsPerPage): static
    {
        $this->itemsPerPage = $itemsPerPage;

        return $this;
    }

    public function getDateFormat(): ?string
    {
        return $this->dateFormat;
    }

    public function setDateFormat(string $dateFormat): static
    {
        $this->dateFormat = $dateFormat;

        return $this;
    }

    public function getTimeFormat(): ?string
    {
        return $this->timeFormat;
    }

    public function setTimeFormat(string $timeFormat): static
    {
        $this->timeFormat = $timeFormat;

        return $this;
    }
}
