#this code is used on training data only.
#this code will make separate IMDB review text files for each review from single IMDB reviews file.
#Also, last character in each review is rating. So, when rating is less than 4, review text file will be moved to negative folder else positive folder.
#for testing purpose, we have kept original 15 reviews only in IMDB.text file. 
#Although, we have used more than 750 reviews as training data in our core algorithm of checkpoint - 3.

import shutil
import os
import re

fn = open("MovieReview_Train/IMDB.txt","r")
for i, line in enumerate(fn):
    f = open("MovieReview_Train/%i.txt" %i,'w')
    f.writelines(line)
    f.close()

source = 'MovieReview_Train/'
#files = os.listdir(source)
for i, file in enumerate(source):
    with open("MovieReview_Train/%i.txt" %i, 'rb+') as imdbreview:
        imdbreview.seek(-4,2) #find last characters in text file
        lastchar = imdbreview.read()
        rating = int(filter(str.isdigit, lastchar)) #keep only number in last characters
        imdbreview.close()
        if (rating > 4):
            shutil.move("MovieReview_Train/%i.txt" %i, "MovieReview_Train/Positive/%i_%i.txt" % (i, rating))
            print "%i_%i.txt" % (i, rating), "Positive"
        elif (rating <= 4):
            shutil.move("MovieReview_Train/%i.txt" %i, "MovieReview_Train/Negative/%i_%i.txt" % (i, rating))
            print "%i_%i.txt" % (i, rating), "Negative"
			
			