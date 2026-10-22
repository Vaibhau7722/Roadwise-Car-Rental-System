document.addEventListener('DOMContentLoaded',()=>{
  const pickup=document.getElementById('pickupDate');
  const returned=document.getElementById('returnDate');
  const estimate=document.getElementById('estimate');
  const today=new Date();
  const localToday=new Date(today.getTime()-today.getTimezoneOffset()*60000).toISOString().slice(0,10);
  if(pickup){pickup.min=localToday;pickup.addEventListener('change',()=>{if(returned){const next=new Date(`${pickup.value}T00:00:00`);next.setDate(next.getDate()+1);returned.min=next.toISOString().slice(0,10);if(returned.value&&returned.value<=pickup.value)returned.value='';}updateEstimate();});}
  if(returned)returned.addEventListener('change',updateEstimate);
  function updateEstimate(){if(!pickup||!returned||!estimate)return;if(!pickup.value||!returned.value){estimate.textContent='Select your dates for an estimate. The server recalculates the final booking price.';return;}const days=(Date.parse(`${returned.value}T00:00:00`)-Date.parse(`${pickup.value}T00:00:00`))/86400000;if(days<1){estimate.textContent='Return date must be after pickup date.';return;}const rate=Number(document.querySelector('.price-panel strong')?.dataset.dailyRate);estimate.textContent=`Estimated rental: ${days} day${days===1?'':'s'} · $${(rate*days).toFixed(2)}. The server recalculates the final booking price.`;}
});
