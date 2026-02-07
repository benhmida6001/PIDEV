<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260207121301 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE category (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR(255) NOT NULL)');
        $this->addSql('CREATE TABLE materiel (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, owner_id INTEGER NOT NULL, category_id INTEGER NOT NULL, name VARCHAR(255) NOT NULL, description VARCHAR(255) DEFAULT NULL, quantity INTEGER NOT NULL, image VARCHAR(255) DEFAULT NULL, status VARCHAR(255) NOT NULL, available_quantity INTEGER NOT NULL, CONSTRAINT FK_18D2B0917E3C61F9 FOREIGN KEY (owner_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT FK_18D2B09112469DE2 FOREIGN KEY (category_id) REFERENCES category (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('CREATE INDEX IDX_18D2B0917E3C61F9 ON materiel (owner_id)');
        $this->addSql('CREATE INDEX IDX_18D2B09112469DE2 ON materiel (category_id)');
        $this->addSql('CREATE TABLE operation (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, material_id INTEGER NOT NULL, user_id INTEGER NOT NULL, type VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, start_date DATETIME NOT NULL, end_date DATETIME DEFAULT NULL, return_image VARCHAR(255) DEFAULT NULL, CONSTRAINT FK_1981A66DE308AC6F FOREIGN KEY (material_id) REFERENCES materiel (id) NOT DEFERRABLE INITIALLY IMMEDIATE, CONSTRAINT FK_1981A66DA76ED395 FOREIGN KEY (user_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('CREATE INDEX IDX_1981A66DE308AC6F ON operation (material_id)');
        $this->addSql('CREATE INDEX IDX_1981A66DA76ED395 ON operation (user_id)');
        $this->addSql('CREATE TEMPORARY TABLE __temp__user AS SELECT id, email, roles, password, profile_picture FROM user');
        $this->addSql('DROP TABLE user');
        $this->addSql('CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, email VARCHAR(180) NOT NULL, roles CLOB NOT NULL --(DC2Type:json)
        , password VARCHAR(255) NOT NULL, profile_picture VARCHAR(255) DEFAULT NULL)');
        $this->addSql('INSERT INTO user (id, email, roles, password, profile_picture) SELECT id, email, roles, password, profile_picture FROM __temp__user');
        $this->addSql('DROP TABLE __temp__user');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_IDENTIFIER_EMAIL ON user (email)');
        $this->addSql('CREATE TEMPORARY TABLE __temp__messenger_messages AS SELECT id, body, headers, queue_name, created_at, available_at, delivered_at FROM messenger_messages');
        $this->addSql('DROP TABLE messenger_messages');
        $this->addSql('CREATE TABLE messenger_messages (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, body CLOB NOT NULL, headers CLOB NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL --(DC2Type:datetime_immutable)
        , available_at DATETIME NOT NULL --(DC2Type:datetime_immutable)
        , delivered_at DATETIME DEFAULT NULL --(DC2Type:datetime_immutable)
        )');
        $this->addSql('INSERT INTO messenger_messages (id, body, headers, queue_name, created_at, available_at, delivered_at) SELECT id, body, headers, queue_name, created_at, available_at, delivered_at FROM __temp__messenger_messages');
        $this->addSql('DROP TABLE __temp__messenger_messages');
        $this->addSql('CREATE INDEX IDX_75EA56E016BA31DB ON messenger_messages (delivered_at)');
        $this->addSql('CREATE INDEX IDX_75EA56E0E3BD61CE ON messenger_messages (available_at)');
        $this->addSql('CREATE INDEX IDX_75EA56E0FB7336F0 ON messenger_messages (queue_name)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP TABLE category');
        $this->addSql('DROP TABLE materiel');
        $this->addSql('DROP TABLE operation');
        $this->addSql('CREATE TEMPORARY TABLE __temp__messenger_messages AS SELECT id, body, headers, queue_name, created_at, available_at, delivered_at FROM messenger_messages');
        $this->addSql('DROP TABLE messenger_messages');
        $this->addSql('CREATE TABLE messenger_messages (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, body CLOB NOT NULL, headers CLOB NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL --(DC2Type:datetime_immutable)
        , available_at DATETIME NOT NULL --(DC2Type:datetime_immutable)
        , delivered_at DATETIME DEFAULT NULL --(DC2Type:datetime_immutable)
        )');
        $this->addSql('INSERT INTO messenger_messages (id, body, headers, queue_name, created_at, available_at, delivered_at) SELECT id, body, headers, queue_name, created_at, available_at, delivered_at FROM __temp__messenger_messages');
        $this->addSql('DROP TABLE __temp__messenger_messages');
        $this->addSql('CREATE INDEX IDX_75EA56E0FB7336F0 ON messenger_messages (queue_name)');
        $this->addSql('CREATE INDEX IDX_75EA56E0E3BD61CE ON messenger_messages (available_at)');
        $this->addSql('CREATE INDEX IDX_75EA56E016BA31DB ON messenger_messages (delivered_at)');
        $this->addSql('CREATE TEMPORARY TABLE __temp__user AS SELECT id, email, profile_picture, roles, password FROM "user"');
        $this->addSql('DROP TABLE "user"');
        $this->addSql('CREATE TABLE "user" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, email VARCHAR(180) NOT NULL, profile_picture VARCHAR(255) DEFAULT NULL, roles CLOB NOT NULL --(DC2Type:json)
        , password VARCHAR(255) NOT NULL)');
        $this->addSql('INSERT INTO "user" (id, email, profile_picture, roles, password) SELECT id, email, profile_picture, roles, password FROM __temp__user');
        $this->addSql('DROP TABLE __temp__user');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_IDENTIFIER_EMAIL ON "user" (email)');
    }
}
