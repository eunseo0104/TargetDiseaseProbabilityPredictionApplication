from django.shortcuts import render

# Create your views here.
from rest_framework import viewsets
from rest_framework.response import Response
from research21_app.models import Test
from research21_app.serializers import TestSerializer, CsvSerializer
from django.http import HttpResponse
from rest_framework.decorators import api_view
import pandas as pd
from research21_app.models import Data

class TestViewSet(viewsets.ModelViewSet):
    queryset = Test.objects.all()
    serializer_class = TestSerializer

@api_view(['POST'])
def post_api(request):

    if request.method=='POST':
        if 'file' not in request.FILES:
            return Response({}, status = 400)
        inp = pd.read_csv(request.FILES['file'])
        ref = pd.read_csv("C:/Users/bnsy3/Downloads/app_data/itrc_snp_hypertension_sm.csv")
        result = pd.merge(inp, ref, on="SNP", how="inner")
        r_frame = pd.DataFrame(result)
        pMaxValue = r_frame.groupby('geno')['P.VAL'].agg(**{'pMax':'max'})
        pMinValue = r_frame.groupby(['geno'])['P.VAL'].agg(**{'pMin':'min'})
        gCnt = r_frame.groupby(['geno'])['P.VAL'].agg(**{'gCount':'count'})
        print(result)
        print(pMaxValue.iloc[0]['pMax'], pMinValue, gCnt)

        arr=[]
        for i in range(0,3):
            data = Data(pMin=pMinValue.iloc[i]['pMin'],pMax=pMaxValue.iloc[i]['pMax'],gCount=gCnt.iloc[i]['gCount'])
            arr.append(data)
        print(CsvSerializer(arr, many=True).data)
        return Response(CsvSerializer(arr, many=True).data, status=200)
