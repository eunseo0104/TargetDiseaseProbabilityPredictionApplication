from django.urls import path, include
from . import views
app_name = 'research21_app'
urlpatterns = [
    path('', include('rest_framework.urls', namespace='rest_framework_category')),
    path('csv', views.post_api)
]