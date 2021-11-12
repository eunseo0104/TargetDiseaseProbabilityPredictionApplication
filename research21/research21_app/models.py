from django.db import models

# Create your models here.

class Test(models.Model):
    test = models.CharField(max_length=10)

    def __str__(self):
        return self.test

class Data(models.Model):
    pMin = models.FloatField()
    pMax = models.FloatField()
    gCount = models.IntegerField()

    def __init__(self, pMin, pMax, gCount):
        self.pMin=pMin
        self.pMax=pMax
        self.gCount=gCount


