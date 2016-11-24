(function() {
  var Paths, StatWatcher, assert, async, events, fs, pathsIn, _pathsIn,
    __hasProp = Object.prototype.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor; child.__super__ = parent.prototype; return child; };

  fs = require('fs');

  events = require('events');

  assert = require('assert');

  async = require('async');

  Paths = (function() {

    function Paths() {
      this.items = [];
      this.itemsDict = {};
      this.numItems = 0;
      this.pos = 0;
    }

    Paths.prototype.add = function(x) {
      assert.ok(!this.contains(x));
      this.items.push(x);
      this.numItems += 1;
      return this.itemsDict[x] = true;
    };

    Paths.prototype.contains = function(x) {
      return this.itemsDict[x] != null;
    };

    Paths.prototype.next = function() {
      if (this.items.length === 0) return null;
      this.pos = (this.pos + 1) % this.numItems;
      return this.items[this.pos];
    };

    return Paths;

  })();

  exports.StatWatcher = StatWatcher = (function(_super) {

    __extends(StatWatcher, _super);

    function StatWatcher(top, opt) {
      var options,
        _this = this;
      events.EventEmitter.call(this);
      options = opt || {};
      this.ignore = opt.ignore != null ? new RegExp(opt.ignore) : null;
      this.match = opt.match != null ? new RegExp(opt.match) : null;
      this.sampleRate = opt['sample-rate'] != null ? 1 * opt['sample-rate'] : 5;
      this.maxStatsPending = 10;
      this.paths = new Paths();
      this.paths.add(top);
      this.path_mtime = {};
      this.numStatsPending = 0;
      this.preexistingPathsToReport = {};
      this.numPreexistingPathsToReport = 0;
      pathsIn(top, function(paths) {
        var path, _i, _len;
        for (_i = 0, _len = paths.length; _i < _len; _i++) {
          path = paths[_i];
          if ((!_this.paths.contains(path)) && (!_this.ignore || !path.match(_this.ignore)) && (!_this.match || path.match(_this.match))) {
            _this.preexistingPathsToReport[path] = true;
            _this.numPreexistingPathsToReport++;
            _this.paths.add(path);
            _this.statPath(path);
          }
        }
        return _this.intervalId = setInterval((function() {
          return _this.tick();
        }), _this.sampleRate);
      });
    }

    StatWatcher.prototype.end = function() {
      return clearInterval(this.intervalId);
    };

    StatWatcher.prototype.tick = function() {
      var path;
      if (this.numStatsPending <= this.maxStatsPending) {
        path = this.paths.next();
        if (path) return this.statPath(path);
      }
    };

    StatWatcher.prototype.statPath = function(path) {
      var _this = this;
      this.numStatsPending++;
      return fs.stat(path, function(err, stats) {
        var eventName, last_mtime;
        _this.numStatsPending--;
        last_mtime = _this.path_mtime[path] || null;
        if (err) {
          if (err.code === 'ENOENT') {
            if (last_mtime) {
              _this.emit('fileDeleted', path);
              delete _this.path_mtime[path];
            }
          } else {
            throw err;
          }
        } else {
          _this.path_mtime[path] = stats.mtime;
          if (stats.isDirectory()) {
            if ((!last_mtime) || (stats.mtime > last_mtime)) _this.scanDir(path);
          } else {
            if (!last_mtime) {
              eventName = 'fileCreated';
              if (_this.preexistingPathsToReport[path]) {
                eventName = 'filePreexisted';
                delete _this.preexistingPathsToReport[path];
                _this.numPreexistingPathsToReport--;
              }
              _this.emit(eventName, path, stats);
            } else if (stats.mtime > last_mtime) {
              _this.emit('fileModified', path, stats);
            }
          }
        }
        if (_this.numPreexistingPathsToReport === 0) {
          _this.emit('allPreexistingFilesReported');
          return _this.numPreexistingPathsToReport = -1;
        }
      });
    };

    StatWatcher.prototype.scanDir = function(path) {
      var _this = this;
      return fs.readdir(path, function(err, files) {
        var file, path2, _i, _len, _results;
        _results = [];
        for (_i = 0, _len = files.length; _i < _len; _i++) {
          file = files[_i];
          path2 = "" + path + "/" + file;
          if ((!_this.paths.contains(path2)) && (!_this.ignore || !path2.match(_this.ignore)) && (!_this.match || path2.match(_this.match))) {
            _this.paths.add(path2);
            _results.push(_this.statPath(path2));
          } else {
            _results.push(void 0);
          }
        }
        return _results;
      });
    };

    return StatWatcher;

  })(events.EventEmitter);

  _pathsIn = function(path, paths, callback) {
    return fs.readdir(path, function(err, files) {
      if (err && err.code === 'ENOTDIR') {
        paths.push(path);
        return callback();
      }
      if (err) throw err;
      return async.forEach(files, (function(file, cb) {
        return _pathsIn("" + path + "/" + file, paths, cb);
      }), (function(err) {
        if (err) throw err;
        return callback();
      }));
    });
  };

  pathsIn = function(dir, callback) {
    var paths;
    paths = [];
    return _pathsIn(dir, paths, function() {
      return callback(paths);
    });
  };

}).call(this);
