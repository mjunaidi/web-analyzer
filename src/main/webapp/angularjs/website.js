'use strict';

/* Directives */
var websiteModule = angular.module('myApp.website', ['ngRoute', 'ngResource']);

websiteModule.run(['WebsiteService', function(WebsiteService) {
  WebsiteService.init();
}]);

websiteModule.factory('WebsiteService', ['$rootScope', '$http', 'Storage', function($rootScope, $http, Storage) {
  var WebsiteService = {};
  
  WebsiteService.websites = false;
  WebsiteService.websiteNames = false;
  WebsiteService.dateKeys = false;

  WebsiteService.searchedWebsiteNames = false;
  WebsiteService.searchedWebsiteDateKeys = false;
  
  WebsiteService.dateKey = false;
  WebsiteService.topWebsites = false;
  
  WebsiteService.websiteName = false;
  WebsiteService.websiteData = false;
  
  WebsiteService.loaded = false;
  WebsiteService.dir = 'datasource/';
  
  WebsiteService.init = function() {
    var url = WebsiteService.dir + "websites.json";
    WebsiteService.readFile(url);
    WebsiteService.initHelper();
  };
  
  WebsiteService.initHelper = function(filename) {
    var websiteNamesUrl = WebsiteService.dir + "websiteNames.json";
    var dateKeysUrl = WebsiteService.dir + "dateKeys.json";
    WebsiteService.readWebsiteNames(websiteNamesUrl);
    WebsiteService.readDateKeys(dateKeysUrl);
  };
  
  WebsiteService.setWebsites = function(data) {
    WebsiteService.websites = data;
  };
  
  WebsiteService.setSearchedWebsiteNames = function(data) {
    WebsiteService.searchedWebsiteNames = data;
  };
  
  WebsiteService.setSearchedWebsiteDateKeys = function(data) {
    WebsiteService.searchedWebsiteDateKeys = data;
  };
  
  WebsiteService.setLoaded = function(loaded) {
    $rootScope.$broadcast('WebsiteLoaded');
  };
  
  WebsiteService.setSearchedWebsiteNamesLoaded = function() {
    $rootScope.$broadcast('SearchedWebsiteNamesLoaded');
  };
  
  WebsiteService.setSearchedWebsiteDateKeysLoaded = function() {
    $rootScope.$broadcast('SearchedWebsiteDateKeysLoaded');
  };
  
  WebsiteService.broadCast = function(value) {
    $rootScope.$broadcast(value);
  };
  
  WebsiteService.readFiles = function(url) {
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.readFilesHelper(data, 0);
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  WebsiteService.readFilesHelper = function(files, ind) {
    var len = files.length;
    if (ind < len) {
      var file = files[ind];
      var path = WebsiteService.dir + file.name;
      ind++;
      WebsiteService.readData(path, files, ind);
    } else {
      WebsiteService.setLoaded(true);
    }
  };
  
  WebsiteService.readData = function(url, files, ind) {
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.setWebsites(data);
        
        WebsiteService.readFilesHelper(files, ind);
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  WebsiteService.readFile = function(url) {
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.setWebsites(data);
        WebsiteService.setLoaded(true);
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  WebsiteService.readWebsiteNames = function(url, broadcast) {
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.websiteNames = data;
        WebsiteService.broadCast('WebsiteNamesLoaded');
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  WebsiteService.readDateKeys = function(url, broadcast) {
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.dateKeys = data;
        WebsiteService.broadCast('DateKeysLoaded');
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  WebsiteService.getTopWebsitesOn = function(dateKey) {
    var url = WebsiteService.dir + dateKey + ".json";
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.dateKey = dateKey;
        WebsiteService.topWebsites = data;
        WebsiteService.broadCast('TopWebsitesUpdated');
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  WebsiteService.getWebsiteData = function(name) {
    var url = WebsiteService.dir + name + ".json";
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.websiteName = name;
        WebsiteService.websiteData = data;
        WebsiteService.broadCast('WebsiteDataUpdated');
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  WebsiteService.deleteWebsite = function(id) {
    var url = '/website/api/delete/' + id;
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.init();
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  WebsiteService.searchWebsiteName = function(name) {
    var url = '/website/api/search/name/' + name;
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.setSearchedWebsiteNames(data);
        WebsiteService.setSearchedWebsiteNamesLoaded();
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  WebsiteService.searchWebsiteDateKey = function(dateKey) {
    var url = '/website/api/search/dateKey/' + dateKey;
    $http.get(url).
      success(function(data, status, headers, config) {
        WebsiteService.setSearchedWebsiteDateKeys(data);
        WebsiteService.setSearchedWebsiteDateKeysLoaded();
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  return WebsiteService;
  
}]);