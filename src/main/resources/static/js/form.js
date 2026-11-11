
document.addEventListener("DOMContentLoaded", () => {
    console.log("FORM JS CHARGÉ");

    const form = document.getElementById("mform");
    const msg = document.getElementById("msg");

    if (!form || !msg) {
        console.error("Formulaire mform ou message msg introuvable !");
        return;
    }

    const submitBtn = form.querySelector('button[type="submit"]');
    const API_URL = "http://localhost:8082/api/candidatures";

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        msg.textContent = "";
        msg.style.color = "";

        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        const confirmation = form.querySelector('[name="ok"]');

        if (!confirmation || !confirmation.checked) {
            msg.textContent = "Veuillez confirmer l'exactitude de vos informations.";
            msg.style.color = "red";
            return;
        }

        const notes = form.querySelector('[name="f_notes"]');

        if (!notes || notes.files.length === 0) {
            msg.textContent = "Veuillez joindre les relevés de notes.";
            msg.style.color = "red";
            return;
        }

        const formData = new FormData(form);
        formData.delete("ok");

        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.textContent = "Envoi en cours...";
        }

        try {
            console.log("Envoi vers :", API_URL);

            const response = await fetch(API_URL, {
                method: "POST",
                body: formData
            });

            // Lire la réponse comme texte pour afficher les erreurs réelles
            const responseText = await response.text();
            console.log("Réponse brute du serveur :", responseText);

            let result;

            try {
                result = JSON.parse(responseText);
            } catch {
                throw new Error(
                    "Le serveur n'a pas retourné du JSON. HTTP " +
                    response.status + " : " + responseText.substring(0, 300)
                );
            }

            if (!response.ok || result.ok !== true) {
                throw new Error(result.message || "Erreur HTTP " + response.status);
            }

            msg.textContent = "✓ " + result.message +
                " Référence : " + result.ref;
            msg.style.color = "green";

            form.reset();

        } catch (error) {
            console.error("Erreur candidature :", error);
            msg.textContent = "Erreur : " + error.message;
            msg.style.color = "red";

        } finally {
            if (submitBtn) {
                submitBtn.disabled = false;
                submitBtn.textContent = "Envoyer ma candidature";
            }
        }
    });
});
