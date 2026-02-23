<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260222201620 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE operation ADD COLUMN returned_image VARCHAR(255) DEFAULT NULL');
        $this->addSql('ALTER TABLE operation ADD COLUMN verification_result CLOB DEFAULT NULL');
        $this->addSql('ALTER TABLE operation ADD COLUMN returned_at DATETIME DEFAULT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TEMPORARY TABLE __temp__operation AS SELECT id, materiel_id, requester_id, type, quantity, description, status, start_date, end_date, created_at FROM operation');
        $this->addSql('DROP TABLE operation');
        $this->addSql('CREATE TABLE operation (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, materiel_id INTEGER NOT NULL, requester_id INTEGER NOT NULL, type VARCHAR(255) NOT NULL, quantity INTEGER NOT NULL, description CLOB DEFAULT NULL, status VARCHAR(50) NOT NULL, start_date DATE NOT NULL, end_date DATE NOT NULL, created_at DATETIME NOT NULL --(DC2Type:datetime_immutable)
        , CONSTRAINT FK_1981A66D16880AAF FOREIGN KEY (materiel_id) REFERENCES materiel (id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT FK_1981A66DED442CF4 FOREIGN KEY (requester_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO operation (id, materiel_id, requester_id, type, quantity, description, status, start_date, end_date, created_at) SELECT id, materiel_id, requester_id, type, quantity, description, status, start_date, end_date, created_at FROM __temp__operation');
        $this->addSql('DROP TABLE __temp__operation');
        $this->addSql('CREATE INDEX IDX_1981A66D16880AAF ON operation (materiel_id)');
        $this->addSql('CREATE INDEX IDX_1981A66DED442CF4 ON operation (requester_id)');
    }
}
