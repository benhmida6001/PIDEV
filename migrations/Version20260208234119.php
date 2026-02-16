<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260208234119 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE categorie (id_categorie INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nom_categorie VARCHAR(50) NOT NULL, instruction_tri CLOB DEFAULT NULL, score_priorite INTEGER DEFAULT NULL, temps_decomposition VARCHAR(100) DEFAULT NULL)');
        $this->addSql('CREATE TABLE signalement (id_signalement INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, image_url VARCHAR(255) DEFAULT NULL, description CLOB NOT NULL, localisation VARCHAR(255) NOT NULL, date_creation DATETIME NOT NULL, statut_traitement VARCHAR(35) NOT NULL, date_traitement DATETIME DEFAULT NULL, id_user INTEGER DEFAULT NULL, id_categorie INTEGER NOT NULL, CONSTRAINT FK_F4B55114C9486A13 FOREIGN KEY (id_categorie) REFERENCES categorie (id_categorie) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('CREATE INDEX fk_sig_cat ON signalement (id_categorie)');
        $this->addSql('CREATE TABLE messenger_messages (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, body CLOB NOT NULL, headers CLOB NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL, available_at DATETIME NOT NULL, delivered_at DATETIME DEFAULT NULL)');
        $this->addSql('CREATE INDEX IDX_75EA56E0FB7336F0E3BD61CE16BA31DBBF396750 ON messenger_messages (queue_name, available_at, delivered_at, id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP TABLE categorie');
        $this->addSql('DROP TABLE signalement');
        $this->addSql('DROP TABLE messenger_messages');
    }
}
