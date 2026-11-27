# Frontend « Après mon bac »

Interface mobile-first (React 19 · TypeScript · Vite) qui consomme l'API REST du backend
(`/api/v1`). Vue d'ensemble du projet : [`../README.md`](../README.md).

## Prérequis

Node.js 22 (ou 20.19+, minimum requis par Vite 8) et npm.

## Commandes

```bash
npm ci                # installe les dépendances figées par package-lock.json
npm run dev           # serveur de développement sur http://localhost:5173
npm run format:check  # vérifie le format (Prettier) ; `npm run format` corrige
npm run lint          # analyse statique (oxlint)
npm test              # tests unitaires (Vitest)
npm run build         # vérification des types (tsc) puis build de production dans dist/
```

## Configuration

L'URL de l'API se règle avec `VITE_API_BASE_URL` (voir `.env.example`). Par défaut :
`http://localhost:8080/api/v1` en développement, `/api/v1` en production (`.env.production`,
frontend et API servis depuis la même origine).
