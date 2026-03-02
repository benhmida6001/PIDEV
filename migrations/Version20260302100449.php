<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260302100449 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TEMPORARY TABLE __temp__user_preferences AS SELECT id, language, theme, notifications, email_notifications, sound_effects, date_format, time_format, items_per_page FROM user_preferences');
        $this->addSql('DROP TABLE user_preferences');
        $this->addSql('CREATE TABLE user_preferences (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER DEFAULT NULL, language VARCHAR(10) NOT NULL, theme VARCHAR(20) NOT NULL, notifications BOOLEAN NOT NULL, email_notifications BOOLEAN NOT NULL, sound_effects BOOLEAN NOT NULL, date_format VARCHAR(20) NOT NULL, time_format VARCHAR(10) NOT NULL, items_per_page INTEGER NOT NULL, CONSTRAINT FK_402A6F60A76ED395 FOREIGN KEY (user_id) REFERENCES "user" (id) NOT DEFERRABLE INITIALLY IMMEDIATE)');
        $this->addSql('INSERT INTO user_preferences (id, language, theme, notifications, email_notifications, sound_effects, date_format, time_format, items_per_page) SELECT id, language, theme, notifications, email_notifications, sound_effects, date_format, time_format, items_per_page FROM __temp__user_preferences');
        $this->addSql('DROP TABLE __temp__user_preferences');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_402A6F60A76ED395 ON user_preferences (user_id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TEMPORARY TABLE __temp__user_preferences AS SELECT id, language, theme, notifications, email_notifications, sound_effects, items_per_page, date_format, time_format FROM user_preferences');
        $this->addSql('DROP TABLE user_preferences');
        $this->addSql('CREATE TABLE user_preferences (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, language VARCHAR(10) NOT NULL, theme VARCHAR(20) NOT NULL, notifications BOOLEAN NOT NULL, email_notifications BOOLEAN NOT NULL, sound_effects BOOLEAN NOT NULL, items_per_page INTEGER NOT NULL, date_format VARCHAR(20) NOT NULL, time_format VARCHAR(10) NOT NULL)');
        $this->addSql('INSERT INTO user_preferences (id, language, theme, notifications, email_notifications, sound_effects, items_per_page, date_format, time_format) SELECT id, language, theme, notifications, email_notifications, sound_effects, items_per_page, date_format, time_format FROM __temp__user_preferences');
        $this->addSql('DROP TABLE __temp__user_preferences');
    }
}
