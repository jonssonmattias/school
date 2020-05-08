console.log("Uppgift 1.");

console.log(5 * 2 <= 12);
console.log(55 > 22);
console.log(16 / 4 == 4);
console.log(8 + 2 < 128);
console.log(32 * 8 != 255);


console.log("Uppgift 2");

console.log("Tisdag".substring(0,3));
console.log("Hamburgare".substring(3));
console.log("I'll be back!".substring(5, 12));


console.log("Uppgift 3.");

console.log("It's Learning".substring(5).toUpperCase());
console.log("JavaScript: The Good Parts".substring(16, 26).toLowerCase());
console.log("JavaScript: The Good Parts".substring(16, 20).toUpperCase());
console.log("Eloquent JavaScript".substring(5, 13));


console.log("Uppgift 4.");

let numbers = [128, 256, 512, 1024, 2048];
let sum = numbers.reduce((a, b) => a + b, 0);
let avg = sum/numbers.length;

console.log(sum);
console.log(avg);


console.log("Uppgift 5.");

let countries = ["Sweden", "Denmark", "Finland", "Norway"];
let a = countries[1].substring(0, 3);
let length = 0;
for(var i = 0; i<countries.length; i++){
    length += countries[i].length;
}
console.log(a);
console.log(length/countries.length);
