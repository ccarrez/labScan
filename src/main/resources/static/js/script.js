function uploadFile() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];
    if (!file) {
        alert('Please select a file.');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    const url = file.name.toLowerCase().endsWith('.pdf') ? '/ocr/pdf' : '/ocr/image';

    fetch(url, {
        method: 'POST',
        body: formData
    })
    .then(response => response.text())
    .then(text => {
        document.getElementById('result').textContent = text;
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('result').textContent = 'Error: ' + error.message;
    });
}