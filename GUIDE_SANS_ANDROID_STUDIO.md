# Générer l'APK sans installer Android Studio

Cette méthode utilise GitHub Actions. L'application est compilée en ligne, puis tu télécharges l'APK.

## Étapes

1. Crée un compte GitHub si tu n'en as pas.
2. Crée un nouveau dépôt GitHub, par exemple : `MaherFocus`.
3. Décompresse le fichier `MaherFocus.zip` sur ton ordinateur.
4. Envoie tout le contenu du dossier `MaherFocus` dans le dépôt GitHub.
5. Dans GitHub, ouvre l'onglet **Actions**.
6. Clique sur **Build Maher Focus APK**.
7. Clique sur **Run workflow**.
8. Attends la fin de la compilation.
9. Ouvre le résultat de l'exécution.
10. Dans **Artifacts**, télécharge `MaherFocus-debug-apk`.
11. Décompresse le fichier téléchargé.
12. Tu obtiendras `app-debug.apk`.
13. Envoie ce fichier sur ton téléphone Android.
14. Ouvre-le sur le téléphone et accepte l'installation depuis une source inconnue si Android le demande.

## Important

- L'APK généré est un APK debug. Il est installable sur ton téléphone pour usage personnel.
- Ce n'est pas encore une version Play Store.
- Pour une version officielle publiée sur Play Store, il faudra plus tard générer une version release signée.

## Si l'installation est bloquée sur le téléphone

Sur Android, va dans :

Paramètres > Sécurité / Confidentialité > Installer des applications inconnues

Puis autorise l'application utilisée pour ouvrir l'APK, par exemple Chrome, Gmail, Drive ou Gestionnaire de fichiers.
