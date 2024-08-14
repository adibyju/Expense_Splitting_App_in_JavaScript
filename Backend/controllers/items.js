const express = require("express");
const router = express.Router();

const {validateToken} = require("./middlewares/loginMiddleware")
const {addEvent,getEvents,getEventDetails} = require("../services/events")
const {addParicipant, getParticipants, removeParticipant} = require("../services/participants")
const {addItem, getItems, deleteItem, updateItem, getItem} = require("../services/items")
const {getId} = require("../services/users")



function paymentcreate(data)
{
 let N=data.payers.length
 let p = new Array(N), o = new Array(N), posi = [], negi = [], pr = [];

 // Input the arrays
 for(let i = 0; i < N; i++){
     p[i] = data.payers[i].amount;
 }

 for(let i = 0; i < N; i++){
     o[i] = data.owers[i].amount;
 }

 // Find the indices of people who owe and are owed money
 for(let i = 0; i < 10; i++){
     if(p[i] - o[i] < 0){
         negi.push(i);
     }
     else if(p[i] - o[i] > 0){
         posi.push(i);
     }
 }

 let i = 0, j = 0;
 while(i < posi.length && j < negi.length){
     if(p[posi[i]] - o[posi[i]] <= o[negi[j]] - p[negi[j]]){
         pr.push([negi[j], [p[posi[i]] - o[posi[i]], posi[i]]]);
         p[negi[j]] += p[posi[i]] - o[posi[i]];
         i++;
     }
     else if(p[posi[i]] - o[posi[i]] > o[negi[j]] - p[negi[j]]){
         pr.push([negi[j], [o[negi[j]] - p[negi[j]], posi[i]]]);
         p[posi[i]] -= o[negi[j]] - p[negi[j]];
         j++;
     }
 }

 // Output the list of transactions
 for(let i = 0; i < pr.length; i++){
     console.log(pr[i][0] + " to pay " + (pr[i][1][1]) + " : " + pr[i][1][0]);
 }
 let payments = []
 for(let i = 0; i < pr.length; i++)
 {
  payments.push(
   {
    payer_id: data.payers[pr[i][0]].user_id,
    reciever_id: data.payers[pr[i][1][1]].user_id,
    amount: pr[i][1][0],
   }
  )
 }
 return payments
}

router.get("/item/:itemID", validateToken, async (req,res) => {
 
 // console.log("get req for ", req.params.itemID)
 response = await getItem({item_id: req.params.itemID})

 res.json(response)
})

router.post("/create", validateToken, async (req,res) => {
 console.log(req.body)

 const param = {
  name: req.body.name,
  description: req.body.descr,
  expense: req.body.expense,
  event_id: req.body.event_id,
  user_id: req.session.user_id,
 }

 response = await addItem(param)

 res.json({ new_item : response})
})

router.post("/:itemID/update", validateToken, async (req,res) => {
 console.log(req.body)

 paymentcreate(req.body)

 const param = {
  item_id: req.params.itemID,
  name: req.body.name,
  description: req.body.descr,
  expense: req.body.expense,

  payers: req.body.payers,
  owers: req.body.owers,

  payments: paymentcreate(req.body)
 } 

 console.log(param.payments)
 response = await updateItem(param)

 res.json({success:true})
})

router.post("/:itemID/delete", validateToken, async (req,res) => {
 // console.log(req.body)

 // const param = {
 //  name: req.body.name,
 //  description: req.body.descr,
 //  expense: req.body.expense,
 //  event_id: req.body.event_id,
 //  user_id: req.session.user_id,
 // }

 response = await deleteItem({item_id:req.params.itemID})

 res.json({success: true})
})

module.exports = router
