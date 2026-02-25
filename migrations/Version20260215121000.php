<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260215121000 extends AbstractMigration
{
    public function getDescription(): string
    {
        return 'Add nom, prenom, adresse, age fields to user table';
    }

    public function up(Schema $schema): void
    {
        $this->addSql('ALTER TABLE "user" ADD nom VARCHAR(100) DEFAULT NULL');
        $this->addSql('ALTER TABLE "user" ADD prenom VARCHAR(100) DEFAULT NULL');
        $this->addSql('ALTER TABLE "user" ADD adresse VARCHAR(255) DEFAULT NULL');
        $this->addSql('ALTER TABLE "user" ADD age INTEGER DEFAULT NULL');
    }

    public function down(Schema $schema): void
    {
        $this->addSql('ALTER TABLE "user" DROP nom');
        $this->addSql('ALTER TABLE "user" DROP prenom');
        $this->addSql('ALTER TABLE "user" DROP adresse');
        $this->addSql('ALTER TABLE "user" DROP age');
    }
}
