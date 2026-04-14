<?php

namespace App\EventSubscriber;

use Symfony\Component\EventDispatcher\EventSubscriberInterface;
use Symfony\Component\HttpKernel\Event\RequestEvent;
use Symfony\Component\HttpKernel\KernelEvents;

class LocaleSubscriber implements EventSubscriberInterface
{
    public static function getSubscribedEvents(): array
    {
        return [
            KernelEvents::REQUEST => ['onKernelRequest', 15],
        ];
    }

    public function onKernelRequest(RequestEvent $event): void
    {
        $request = $event->getRequest();
        
        // Ne pas appliquer sur les requêtes AJAX ou partielles
        if ($request->isXmlHttpRequest() || $request->attributes->get('_route') === null) {
            return;
        }
        
        if (!$request->hasSession()) {
            return;
        }

        $session = $request->getSession();
        $locale = $session->get('_locale', 'fr');
        
        $request->setLocale($locale);
    }
}
