# Maher Focus

Première version fonctionnelle d’une application Android locale pour réduire la surcharge mentale et gérer les tâches quotidiennes.

## Ce que contient cette V1

- Kotlin
- Jetpack Compose
- Room Database locale
- Navigation Compose
- WorkManager pour 3 rappels locaux maximum
- Fonctionnement hors ligne
- Aucun compte utilisateur
- Aucun cloud

## Écrans inclus

1. Aujourd’hui
   - tâches obligatoires aujourd’hui
   - tâches importantes cette semaine
   - tâches en attente / bloquées
   - progression du jour
   - mode Journée difficile limité à 3 tâches

2. Ajout rapide
   - titre
   - catégorie
   - priorité adaptée au TDAH
   - date limite optionnelle
   - note optionnelle
   - tâche récurrente ou non

3. Vide-tête
   - capture rapide des idées
   - conversion d’une idée en tâche

4. Objectifs
   - objectif par domaine
   - petites étapes
   - progression simple

5. Famille / Enfants
   - Ahmed
   - Amen Allah
   - tâches par enfant : école, français, mathématiques, santé, rendez-vous, documents, sport

6. École / Travail
   - préparation de cours
   - corrections
   - examens
   - messages aux parents
   - remédiations
   - groupes d’élèves
   - idées de simulations ou ressources numériques

7. Immigration / Démarches
   - documents à suivre
   - statut : à faire, envoyé, en attente, reçu, problème
   - date limite
   - note importante

8. Routines
   - routines quotidiennes, hebdomadaires ou personnalisées

9. Bilan du soir
   - terminé aujourd’hui
   - blocage
   - priorité de demain

10. Rappels
   - matin
   - après l’école
   - soir
   - tous désactivables

## Ouvrir le projet dans Android Studio

1. Dézipper le fichier `MaherFocus.zip`.
2. Ouvrir Android Studio.
3. Choisir `File > Open`.
4. Sélectionner le dossier `MaherFocus`.
5. Laisser Android Studio synchroniser Gradle.
6. Si Android Studio propose de mettre à jour Gradle ou le plugin Android, accepter seulement si tu veux utiliser les versions installées sur ta machine.

## Générer un APK

Dans Android Studio :

1. Ouvrir le projet.
2. Attendre la fin de la synchronisation Gradle.
3. Aller dans `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
4. Quand la compilation termine, cliquer sur `locate` pour trouver l’APK.

Chemin habituel :

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Limites assumées de cette V1

Cette première version est volontairement simple. Elle ne contient pas de cloud, pas d’intelligence artificielle intégrée, pas de synchronisation Google Calendar/Gmail, pas de statistiques lourdes et pas de gamification. C’est un choix : l’objectif est d’avoir une base utilisable et calme, pas une application qui ajoute de la charge mentale.

## Prochaines améliorations utiles

- Ajouter un vrai sélecteur de date au lieu du format texte AAAA-MM-JJ.
- Ajouter une édition complète des tâches existantes.
- Ajouter une vue calendrier simple.
- Ajouter une sauvegarde/export local en fichier.
- Ajouter une meilleure gestion des jours personnalisés pour les routines.

---

## Option sans Android Studio : compiler l'APK avec GitHub Actions

Si tu ne peux pas installer Android Studio, utilise le fichier déjà inclus :

`.github/workflows/build-apk.yml`

Étapes rapides :

1. Crée un dépôt GitHub.
2. Envoie tout le dossier `MaherFocus` dans ce dépôt.
3. Va dans l'onglet **Actions**.
4. Lance **Build Maher Focus APK**.
5. Télécharge l'artifact **MaherFocus-debug-apk**.
6. Décompresse-le et installe `app-debug.apk` sur ton téléphone.

Un guide plus détaillé est dans :

`GUIDE_SANS_ANDROID_STUDIO.md`
