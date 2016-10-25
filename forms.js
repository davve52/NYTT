$(document).ready(function() {
  var form = document.getElementById('forms');
  var createForm = document.createElement('form');
  createForm.setAttribute("action", "server.php");
  createForm.setAttribute("enctype", "multipart/form-data");
  createForm.setAttribute("method", "POST");
  form.appendChild(createForm);

  var heading = document.createElement('h2');
  heading.innerHTML = 'Lägg till vara';
  createForm.appendChild(heading);

  var line = document.createElement('hr');
  createForm.appendChild(line);

  var linebreak = document.createElement('br');
  createForm.appendChild(linebreak);

  var title = document.createElement('label');
  title.innerHTML = 'Titel';
  createForm.appendChild(title);

  var titleInput = document.createElement('input');
  titleInput.setAttribute('type', 'text');
  titleInput.setAttribute('id', 'titleInput');
  titleInput.setAttribute('name', 'title');
  createForm.appendChild(titleInput);

  var linebreak = document.createElement('br');
  createForm.appendChild(linebreak);

  var image = document.createElement('label');
  image.innerHTML = 'Välj bild';
  createForm.appendChild(image);

  var imageInput = document.createElement('input');
  imageInput.setAttribute('type', 'file');
  imageInput.setAttribute('id', 'imageInput');
  imageInput.setAttribute('name', 'image');
  createForm.appendChild(imageInput);

  var linebreak = document.createElement('br');
  createForm.appendChild(linebreak);

  var description = document.createElement('label');
  description.innerHTML = 'Beskriv din vara';
  createForm.appendChild(description);

  var descriptionInput = document.createElement('textarea');
  descriptionInput.setAttribute('type', 'text');
  descriptionInput.setAttribute('id', 'descriptionInput');
  descriptionInput.setAttribute('name', 'desc');
  createForm.appendChild(descriptionInput);

  var linebreak = document.createElement('br');
  createForm.appendChild(linebreak);

  var submitButton = document.createElement('input');
  submitButton.setAttribute('type', 'submit');
  submitButton.setAttribute('id', 'subButton');
  submitButton.setAttribute('value', 'Ladda upp');
  createForm.appendChild(submitButton);

});
