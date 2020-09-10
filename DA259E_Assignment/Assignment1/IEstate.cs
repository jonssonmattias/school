namespace Assignment1
{
    internal interface IEstate
    {
        int ID { get; set; }
        Adress Adress{ get;}
        string[] Print();
    }
}