// Petit carrousel : clique sur un point pour changer la diapositive active
const dots=[...document.querySelectorAll('#dots i')];
dots.forEach((d,i)=>d.addEventListener('click',()=>{dots.forEach(x=>x.classList.remove('on'));d.classList.add('on');}));
let n=1;setInterval(()=>{dots.forEach(x=>x.classList.remove('on'));n=(n+1)%dots.length;dots[n].classList.add('on');},5000);


// Menu : met en évidence la rubrique visible et affiche le bouton "haut de page"
const links=[...document.querySelectorAll('nav a[data-t]')];
function spy(){
let cur=null;
links.forEach(a=>{
  const el=document.getElementById(a.dataset.t);
  if(el&&el.getBoundingClientRect().top<=230) cur=a;
});
links.forEach(a=>a.classList.toggle('on',a===cur));
document.getElementById('topbtn').style.display=scrollY>400?'flex':'none';
}
addEventListener('scroll',spy,{passive:true});
spy();
