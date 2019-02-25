
RecipeFinderTest 测试类：


注意：通用fridge.cvs  无需更改

bread,10,slices,25/12/2019
cheese,10,slices,25/12/20198
butter,250,grams,25/12/2017
peanut butter,250,grams,2/12/2014
mixed salad,150,grams,11/12/2019
  

1. canGetRecipeForToday 测试方法

注意：需要更改fridge.cvs为

bread,10,slices,25/12/2019
cheese,10,slices,25/12/2018
butter,250,grams,25/12/2017
peanut butter,250,grams,2/12/2014
mixed salad,150,grams,26/12/2019


2. canGetRecipeForFutureDate 测试方法


注意：需要更改fridge.cvs为

bread,10,slices,25/12/2019
cheese,10,slices,25/12/2019
butter,250,grams,25/12/2017
peanut butter,250,grams,2/12/2014
mixed salad,150,grams,26/12/2019

3. canGetRecipeWithExpiredIngredients 测试方法

注意：需要更改fridge.cvs为

bread,10,slices,25/12/2018
cheese,10,slices,25/12/20198
butter,250,grams,25/12/2017
peanut butter,250,grams,2/12/2014
mixed salad,150,grams,26/12/2019




注意：如果测试用例报错 则是fridge.cvs 配置文件没配好

报错的信息是程序期望返回什么  而实际返回的是  程序的实际逻辑是正常的


注意：请勿运行 recipe-finder.bat 




