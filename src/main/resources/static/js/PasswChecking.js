/**
 * Created by tijs on 23/11/2016.
 */
function CheckPassword(inputtxt, confirmation)
{
    if(inputtxt == confirmation){
        var passw = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
        if(inputtxt.value.match(passw))
        {
            alert('Correct, try another...')
            return true;
        }
        else
        {
            alert('Wrong...!')
            return false;
        }
    }else{
        return false;
    }
}
