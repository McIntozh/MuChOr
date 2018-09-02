var app = angular.module('app', []);

app.controller('ChannelController', function ChannelController($scope) {
  $scope.channels = [
    {
      key: 'GOOG',
      name: 'Google'
    }
  ];
});

app.controller('ChannelCategoryController', function ChannelCategoryController($scope) {
  $scope.channels = [
    {
      key: 'GOOG',
      name: 'Google'
    }
  ];
});

app.module('core.category', ['ngResource']);
app.
  module('core.category').
  factory('Category', ['$resource',
    function($resource) {
      return $resource('api/category/', {}, {
        query: {
          method: 'GET',
          params: {},
          isArray: true
        }
      });
    }
  ]);