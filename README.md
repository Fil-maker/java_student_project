# Итоговый проект по Java (вариант 10)
## Зубарев Филипп Олегович 
### РИ-210943

Проект сделан при помощи maven и библиотек opencsv, jfreechart и драйвера sqlite-jdbc

Считываем csv файл при помощи библиотеки opencsv и сохраняем данные в объекты класса, хранящего информацию о регионе и в sqlite базу данных

### Задание 1

Считаем данные из базы данных при помощи запроса:

select sr.name, round((100.0 * sum(internet_users) / sum(population)), 2) as 'part' <br>
from city_stat <br>
join sub_regions sr on sr.id = city_stat.sub_region_id <br>
group by sub_region_id <br> order by 1.0 * sum(internet_users) / sum(population) desc

![data](/src/main/resources/img.png)

добавим их в датасет и выведем, сохранив результат в виде изображения при помощи JFreeChart

![plot](/src/main/resources/histogram.png)

### Задание 2

select city_stat.name, min(internet_users) as users <br>
from city_stat <br>
join sub_regions sr on sr.id = city_stat.sub_region_id <br>
where sr.name = 'Western Europe' group by sr.name

Применив этот запрос найдем искомый результат:


Город в Восточной Европе с наименьшим кол-вом зарегистрированных в ин-ете: Liechtenstein (37201)

### Заданеи 3

select name, round((100.0 * internet_users / city_stat.population), 2) as part <br>
from city_stat <br>
where round(1.0 * internet_users / city_stat.population, 2) between 0.75 and 0.85

![data](/src/main/resources/img_1.png)