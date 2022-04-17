<%@ page contentType="text/html; charset=UTF-8" %>

<div id="id01" class="modal">
    <span onclick="closePopup()" class="close" title="Supprimer so compte">&times;</span>
    <form id="accountDeletion" class="modal-content" action="#">
        <div class="container">
            <h1 class="text-danger">Suppression Compte</h1>
            <p class="text-danger">Etes-vous sûr de vouloir supprimer ?</p>
            <p>Veuillez confirmer la suppression en tapant votre adresse mail : </p>
            <input id="emailConfirmation" class="form-control" name="email" type="text">
            <div class="clearfix">
                <button type="button" id="cancel" class="cancelbtn btn btn-primary" onclick="closePopup()" >Annuler, garder le compte</button>
                <button type="submit" id="deleteAccountBtn" class="deletebtn btn btn-primary background-danger">Oui, supprimer le compte</button>
            </div>
        </div>
    </form>
</div>