This I implemented for Seekho Assignment .
I implemented the list of top rated anime using the api provided.
Upon clicking on a list item it navigates to detail screen which displays the details of the anime.
I fetched the data from api using retrofit and stored the data to room database during the screen opening.In case of any error the data from room database is shown.
I show the data from room database into the composables.
I implemented MVVM Architecture and stateflow for reactive data handling and coil library for image loading.

Assumptions made/Limitations:

In the api the embedded url link of videos did not have content so the videos were not playing in some cases.
To get the cast details another api endpoint had to be used than what mentioned in the assignment.

![1000165651](https://github.com/user-attachments/assets/74f9ce9f-dea9-4e23-a012-90428ee01b6c)
![1000165652](https://github.com/user-attachments/assets/6cc89545-08dc-463c-9167-a6041b204a2c)
![1000165650](https://github.com/user-attachments/assets/3ece4542-476b-4852-b3ed-0cc50c7fb773)
