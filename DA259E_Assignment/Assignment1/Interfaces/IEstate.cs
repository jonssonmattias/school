namespace Assignment1
{
    internal interface IEstate
    {
        int ID { get; set; }
        Address Address { get; set; }
        string[] Print();
        string ToSearchableString();
    }
}