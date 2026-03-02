<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260302_110000_AddUserColumnToUserPreferences extends AbstractMigration
{
    public function getDescription(): string
    {
        return 'Add user_id column to user_preferences table and create foreign key';
    }

    public function up(Schema $schema): void
    {
        // Add user_id column to user_preferences table
        $this->addSql('ALTER TABLE user_preferences ADD user_id INT NOT NULL');
        
        // Create foreign key constraint
        $this->addSql('ALTER TABLE user_preferences ADD CONSTRAINT FK_USER_PREFERENCES_USER FOREIGN KEY (user_id) REFERENCES `user` (id)');
        
        // Create unique index to ensure one preference per user
        $this->addSql('CREATE UNIQUE INDEX UNIQ_USER_PREFERENCES_USER ON user_preferences (user_id)');
    }

    public function down(Schema $schema): void
    {
        // Remove foreign key constraint
        $this->addSql('ALTER TABLE user_preferences DROP FOREIGN KEY FK_USER_PREFERENCES_USER');
        
        // Remove unique index
        $this->addSql('DROP INDEX UNIQ_USER_PREFERENCES_USER ON user_preferences');
        
        // Remove user_id column
        $this->addSql('ALTER TABLE user_preferences DROP user_id');
    }
}
