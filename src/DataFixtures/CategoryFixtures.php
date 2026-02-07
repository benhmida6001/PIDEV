<?php

namespace App\DataFixtures;

use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;

class CategoryFixtures extends Fixture
{
    public function load(ObjectManager $manager): void
    {
        $categories = [
            'Gardening & Nature',
            'Cleaning & Recycling',
            'Event Equipment',
            'Others'
        ];
    
        foreach ($categories as $name) {
            $category = new Category();
            $category->setName($name);
            $manager->persist($category);
        }
    
        $manager->flush();
    }
    
}


