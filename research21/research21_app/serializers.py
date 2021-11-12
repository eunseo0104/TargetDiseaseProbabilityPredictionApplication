from rest_framework import serializers
from research21_app.models import Test
from research21_app.models import Data


class TestSerializer(serializers.ModelSerializer):
    class Meta:
        model = Test
        fields = ('test', )

class CsvSerializer(serializers.ModelSerializer):
    class Meta:
        model = Data
        fields = ('pMin','pMax','gCount' )
    pMin = serializers.FloatField()
    pMax = serializers.FloatField()
    gCount = serializers.IntegerField()
