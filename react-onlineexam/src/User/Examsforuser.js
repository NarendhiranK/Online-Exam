import React from 'react'

const Examsforuser = () => {


    fetch("https://localhost/8443/onlineexam/control/ExamsForUsersEvent").then(response=>{
        response.json();

    }).then(data=>console.log(data)).catch(err=>console.log(err));
  return (
    <div>

    </div>
  )
}

export default Examsforuser
