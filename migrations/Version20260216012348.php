<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260216012348 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
    $this->addSql('ALTER TABLE signalement ADD est_archive BOOLEAN DEFAULT 0 NOT NULL');    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TEMPORARY TABLE __temp__signalement AS SELECT id_signalement, image_url, description, localisation, date_creation, statut_traitement, date_traitement, id_user, id_categorie FROM signalement');
        $this->addSql('DROP TABLE signalement');
        $this->addSql('CREATE TABLE signalement (id_signalement INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, image_url VARCHAR(255) DEFAULT NULL, description CLOB NOT NULL, localisation VARCHAR(255) NOT NULL, date_creation DATETIME NOT NULL, statut_traitement VARCHAR(35) NOT NULL, date_traitement DATETIME DEFAULT NULL, id_user INTEGER DEFAULT NULL, id_categorie INTEGER NOT NULL, CONSTRAINT FK_F4B55114C9486A13 FOREIGN KEY (id_categorie) REFERENCES categorie (id_categorie) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO signalement (id_signalement, image_url, description, localisation, date_creation, statut_traitement, date_traitement, id_user, id_categorie) SELECT id_signalement, image_url, description, localisation, date_creation, statut_traitement, date_traitement, id_user, id_categorie FROM __temp__signalement');
        $this->addSql('DROP TABLE __temp__signalement');
        $this->addSql('CREATE INDEX fk_sig_cat ON signalement (id_categorie)');
    }
}
