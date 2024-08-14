const express = require("express");
const router = express.Router();
const bcrypt = require("bcrypt")
const {loginQuery,signupQuery} = require("../services/authentication")

router.get("/", (req,res) => {
  res.send("Authentication router Is Running")
})

router.post("/signup", async (req,res) => {
  console.log(req.body)
  if(!req.body.signupdata)
    return res.json({err: "signup details not provided"})
  
  // const plaintextPassword = req.body.signupdata.password
  // await bcrypt.hash(plaintextPassword, 10).then(hash => {
  //   // Store hash in the database
  //   console.log(hash)
  //   req.body.signupdata.password=hash
  //   });

  response = await signupQuery(req.body.signupdata)
  if(response.err)
  {
    return res.json({success: false , err: response.err})
  }
  else
  {
    return res.json({success: true})
  }
})


router.post("/login", async (req,res) => {
    var password = ''
    var ID = ''
    var err = ''
    var response = ''
    console.log(req.body)
    if(!req.body.logindata)
      return res.json({succes: false, err: "login details not provided"})
    response = await loginQuery(req.body.logindata)
    console.log(response)
    if(response.err){
      console.error(err.stack)
    }else if(response.length!=0){
      password = response[0].password
      ID = response[0].id
    }
    
    console.log(password)
    console.log(req.body.logindata.password)
    if(password===req.body.logindata.password)
    {
      req.session.authorized = true;
      req.session.user_id = ID;
      res.json({succes: true})
    }
    else
    {
      res.json({succes: false, err: "incorrect password"}) 
    }



})

router.post('/logout', async (req,res) => {
  console.log("destroyed")
  
  req.session.destroy();
});

module.exports = router