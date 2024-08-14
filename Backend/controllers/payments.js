const express = require("express");
const router = express.Router();

const {validateToken} = require("./middlewares/loginMiddleware")
const {addPayment, getPayments} = require("../services/payments")

router.get("/", validateToken, async (req,res) => {
 const response = await getPayments({user_id:req.session.user_id})
 res.json({payments:response})
})

router.post("/settle", validateToken, async (req,res) =>{
 if(req.body.other_id && req.body.amount)
 {
  if(req.body.amount<0)
  {
   await addPayment({
    payer_id: req.session.user_id,
    receiver_id: req.body.other_id,
    amount: -req.body.amount
   })
  }
  else
  {
   await addPayment({
    payer_id: req.body.other_id,
    receiver_id: req.session.user_id,
    amount: req.body.amount
   })
  }
  return res.json({success:true})
 }
 return res.json({success:false, msg:"other_id/amount not provided"})
})

module.exports = router
