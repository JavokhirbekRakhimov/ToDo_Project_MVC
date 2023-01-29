function reconnect(){
  setTimeout(function(){
    document.querySelector('.first').classList.toggle('num-four');
    document.querySelector('.second').classList.toggle('num-zero');
    document.querySelector('.third').classList.toggle('num-three');
    document.querySelector('.container').classList.toggle('_403');
    document.querySelector('.redirect').classList.toggle('visible');
  }, 500);
}
function connect(){

  window.location.replace("/");
}



window.addEventListener('load', reconnect);