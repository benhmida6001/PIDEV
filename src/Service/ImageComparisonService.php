<?php

namespace App\Service;

use Symfony\Contracts\HttpClient\HttpClientInterface;

class ImageComparisonService
{
    private HttpClientInterface $httpClient;
    private string $apiKey;

    public function __construct(HttpClientInterface $httpClient, string $apiKey)
    {
        $this->httpClient = $httpClient;
        $this->apiKey = $apiKey;
    }

    public function compareImages(string $originalImagePath, string $returnedImagePath, string $currentStatus): array
    {
        if (empty($this->apiKey)) {
            throw new \Exception('GEMINI_API_KEY not configured');
        }
        
        if (!file_exists($originalImagePath)) {
            throw new \Exception('Original image not found: ' . $originalImagePath);
        }
        
        if (!file_exists($returnedImagePath)) {
            throw new \Exception('Returned image not found: ' . $returnedImagePath);
        }
        
        $originalImageBase64 = base64_encode(file_get_contents($originalImagePath));
        $returnedImageBase64 = base64_encode(file_get_contents($returnedImagePath));

        $prompt = "Comparez ces deux images d'équipement. La première montre l'état original ('{$currentStatus}'). La deuxième montre l'état retourné. Analysez et déterminez le nouvel état. Répondez EXACTEMENT dans ce format: 'statut|explication'. Le statut doit être: excellent, good, acceptable, ou to_check. Exemple: 'good|Quelques rayures mineures mais fonctionnel'";

        try {
            $response = $this->httpClient->request(
                'POST',
                'https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=' . $this->apiKey,
                [
                    'headers' => [
                        'Content-Type' => 'application/json',
                    ],
                    'timeout' => 120,
                    'json' => [
                        'contents' => [
                            [
                                'parts' => [
                                    ['text' => $prompt],
                                    [
                                        'inline_data' => [
                                            'mime_type' => 'image/jpeg',
                                            'data' => $originalImageBase64
                                        ]
                                    ],
                                    [
                                        'inline_data' => [
                                            'mime_type' => 'image/jpeg',
                                            'data' => $returnedImageBase64
                                        ]
                                    ]
                                ]
                            ]
                        ],
                        'generationConfig' => [
                            'temperature' => 0.2,
                            'maxOutputTokens' => 500
                        ]
                    ]
                ]
            );

            if ($response->getStatusCode() !== 200) {
                throw new \Exception('Erreur API Gemini: ' . $response->getContent(false));
            }

            $data = $response->toArray(false);
            $aiResponse = $data['candidates'][0]['content']['parts'][0]['text'] ?? '';

            if (empty($aiResponse)) {
                throw new \Exception('Réponse vide de l\'IA');
            }

            $parts = explode('|', $aiResponse, 2);
            $newStatus = $this->extractStatus($parts[0]);
            $explanation = isset($parts[1]) ? trim($parts[1]) : $aiResponse;

            return [
                'status' => $newStatus,
                'explanation' => $explanation
            ];
        } catch (\Exception $e) {
            return [
                'status' => 'to_check',
                'explanation' => 'Analyse IA impossible: ' . $e->getMessage() . '. Vérification manuelle nécessaire.'
            ];
        }
    }

    private function extractStatus(string $response): string
    {
        $response = strtolower(trim($response));
        $validStatuses = ['excellent', 'good', 'acceptable', 'to_check'];
        
        foreach ($validStatuses as $status) {
            if (stripos($response, $status) !== false) {
                return $status;
            }
        }

        return 'to_check';
    }
}