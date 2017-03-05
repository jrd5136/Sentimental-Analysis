# Sentimental-Analysis
One of the starting points while working on text reviews of movies was to calculate the average size of movie reviews to get some insight on quality of reviews. Also, I wanted to know there should be no review in our training data which has only couple of words such as ‘Awesome movie’, ‘really great movie’ as a review of movie. So, when i counted the words, average was 610 words per review which indicated that i have pretty descriptive reviews which is good for sentimental analysis. Also, some words have high occurrence count in the same review compared to others. i found below 20 words which are most frequent words in positive as well as negative. Below list of most frequent words indicated that alone ‘Bag of Words’ model is not very good model for the sentimental analysis since words – film, like, much are common across positive and negative reviews. 
Negative	      Positive
Movie	Film	    Film	Movie
Like	Even	    Like	Good
Good	Bad     	Great	Story
Watch	Really	  See	Time
Time	See	      Well	Nice
Much	Get	      Really	Feel
People	Story	  Even	Very
Make	Made	    First	Much
poor	First	    Love	People
scene	watch	    Best	Get

Also, total number of unique words was huge (~1,50,000) which prompted to use TF-IDF method for feature extraction. Since the reviews are classified as positive and negative reviews, test data was divided into 500 positive and 500 negative reviews for the analysis. When the Naïve Bayes was run on the data set, accuracy of data was 80.5%.

Class 	TP Rate	  FP Rate	  Precision	  Recall 	  F - Measure
Positive	0.716	  0.284	    0.871	      0.716	    0.785
Negative	0.894	  0.109	    0.758	      0.894	    0.819

Actual
Predicted		Positive	Negative
	Positive	358	      53
	Negative	142	      447


tried to manipulate various parameters to see which affect the accuracy of our model. So, first i removed the stop words from our training data and i observed that accuracy was improved at some level. Second step was to make texts case in-sensitive as that would help to count word occurrence across all reviews and prune the un-important words. Accuracy was increased to 84% after implementing stop words removal and text case in-sensitive steps.
Class 	TP Rate	FP Rate	Precision	Recall 	F - Measure
Positive	0.752	0.248	0.923	0.752	0.841
Negative	0.938	0.062	0.790	0.938	0.857

Actual
Predicted		Positive	Negative
	Positive	376	31
	Negative	124	469

Feature Extraction:
I have used below 3 method for the extraction of meaningful information from the 4000 review texts (Positive reviews – 2000, Negative Reviews - 2000) which further utilized for the training purpose. These features were used on Naïve Bayes classifier to compare the accuracy of the model.
1.	Bag of Words:
As mentioned earlier, I had already counted the total word count across all reviews, first feature set was created on 50,000 most frequent words according to their occurrence. Also, one more feature set was created in a similar way but this time using top 1,00,000 words. For the classification, when i trained the feature set on Naïve Bayes classifier, i got the accuracy 84.8% and 84.9% respectively which indicated no major difference.
2.	N- Gram Modelling
Bag of words primarily concentrates on frequency of each word and ignore the semantic context of the texts. To overcome this issue, i tried N- gram modelling in which i created bi-grams to provide more contextual information on the movie review texts. Here, i have created one feature set similar to ‘Bag of Words’ approach explained above but using the bi-grams. For the classification, when i trained the bi-grams set on naïve Bayes classifier, i got the accuracy 85.5% which again a marginal difference from bag of words approach.
3.	TF-IDF Modelling   
Both approaches mentioned above focused on the high frequency of words for the feature extraction abut completely ignored the most significant – polarity of movie review. In this approach, i calculated TF-IDF score of each word instead of frequency counts. Also, there were couple of words which were common in both positive and negative reviews and won’t contribute to the classifier. i ignored all the common words whose count was more than 50 in both positive and negative reviews. When i trained the model on the same Naïve Bayes classifier, i got 84.4% accuracy and again same as previous approaches but this time TF-IDF score was utilized instead of frequency counts in the model.
 
	
Summary:
Overall, i found that major change in accuracy occurred when i removed the stop words and made text case in-sensitive while applying various feature extraction approaches to Naïve Bayes classifier. Also, i observed that there was no major difference in accuracy when bag of words, bigram and TF-IDF approaches applied to Naïve Bayes algorithm. However, accuracy was decreased little compared to others feature extraction approaches when most common words (occurred more than 50 times) removed from both positive and negative reviews in TF-IDF modelling.
