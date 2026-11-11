
document.addEventListener("DOMContentLoaded", () => {

    console.log("FORM JS CHARGÉ");

    const form = document.getElementById("mform");
    const msg = document.getElementById("msg");

    if (!form || !msg) {
        console.error("Formulaire mform ou message msg introuvable !");
        return;
    }

    const submitBtn = form.querySelector('button[type="submit"]');

    // URL de production Render
    const API_URL = "/api/candidatures";

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        msg.textContent = "";
        msg.style.color = "";

        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        // Vérification de la confirmation
        const confirmation = form.querySelector('[name="ok"]');

        if (!confirmation || !confirmation.checked) {
            msg.textContent =
                "Veuillez confirmer l'exactitude de vos informations.";
            msg.style.color = "red";
            return;
        }

        // Vérification des relevés de notes
        const notes = form.querySelector('[name="f_notes"]');

        if (!notes || !notes.files || notes.files.length === 0) {
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

            const responseText = await response.text();

            console.log("HTTP Status :", response.status);
            console.log("Réponse serveur :", responseText);

            let result;

            try {
                result = JSON.parse(responseText);
            } catch {
                throw new Error(
                    "Réponse invalide du serveur. HTTP " +
                    response.status + " : " +
                    responseText.substring(0, 300)
                );
            }

            if (!response.ok) {
                throw new Error(
                    result.message || "Erreur HTTP " + response.status
                );
            }

            if (result.ok !== true) {
                throw new Error(
                    result.message || "La candidature n'a pas été enregistrée."
                );
            }

            msg.textContent =
                "✓ Votre candidature a été envoyée avec succès ! " +
                (result.ref ? "Référence : " + result.ref : "");

            msg.style.color = "green";

            form.reset();

        } catch (error) {

            console.error("Erreur candidature :", error);

            msg.textContent =
                "Échec de l'envoi : " + error.message;

            msg.style.color = "red";

        } finally {

            if (submitBtn) {
                submitBtn.disabled = false;
                submitBtn.textContent = "Envoyer ma candidature";
            }
        }
    });
});
