define(function (require) {

  var assert = require('intern/chai!assert');
  var fs = require('intern/dojo/node!fs');
  var Command = require('intern/dojo/node!leadfoot/Command');
  var pollUntil = require('intern/dojo/node!leadfoot/helpers/pollUntil');

  var DEFAULT_TIMEOUT = 30000;

  Command.prototype.checkElementCount = function (selector, count) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector, count) {
            var elements = document.querySelectorAll(selector);
            return elements.length === count ? true : null;
          }, [selector, count], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to find ' + count + ' elements by selector "' + selector + '"');
          });
    });
  };

  Command.prototype.checkElementExist = function (selector) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector) {
            var elements = document.querySelectorAll(selector);
            return elements.length > 0 ? true : null;
          }, [selector], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to find elements by selector "' + selector + '"');
          });
    });
  };

  Command.prototype.checkElementNotExist = function (selector) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector) {
            var elements = document.querySelectorAll(selector);
            return elements.length === 0 ? true : null;
          }, [selector], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to fail to find elements by selector "' + selector + '"');
          });
    });
  };

  Command.prototype.waitForDeletedByCssSelector = function (selector) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector) {
            var elements = document.querySelectorAll(selector);
            return elements.length === 0 ? true : null;
          }, [selector], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to fail to find elements by selector "' + selector + '"');
          });
    });
  };

  Command.prototype.checkElementInclude = function (selector, text) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector, text) {
            var elements = Array.prototype.slice.call(document.querySelectorAll(selector));
            var result = elements.some(function (element) {
              return element.textContent.indexOf(text) !== -1;
            });
            return result ? true : null;
          }, [selector, text], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to find elements by selector "' + selector +
                '" that include "' + text + '"');
          });
    });
  };

  Command.prototype.checkElementNotInclude = function (selector, text) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector, text) {
            var elements = Array.prototype.slice.call(document.querySelectorAll(selector));
            var result = elements.every(function (element) {
              return element.textContent.indexOf(text) === -1;
            });
            return result ? true : null;
          }, [selector, text], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to fail to find elements by selector "' + selector +
                '" that include "' + text + '"');
          });
    });
  };

  Command.prototype.clickElement = function (selector) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector) {
            var elements = document.querySelectorAll(selector);
            if (elements.length > 0) {
                jQuery(selector).first().click();
                return true;
            }
            return null;
          }, [selector], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to click by selector "' + selector + '"');
          });
    });
  };

  Command.prototype.mouseUp = function (selector) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector) {
            var elements = document.querySelectorAll(selector);
            if (elements.length > 0) {
                jQuery(selector).mouseup();
                return true;
            }
            return null;
          }, [selector], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to mouseUp by selector "' + selector + '"');
          });
    });
  };

  Command.prototype.trigger = function (selector, what) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector, what) {
            var elements = document.querySelectorAll(selector);
            if (elements.length > 0) {
                jQuery(selector).trigger(what);
                return true;
            }
            return null;
          }, [selector, what], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to trigger by selector "' + selector + '"');
          });
    });
  };

  Command.prototype.changeElement = function (selector) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector) {
            var elements = document.querySelectorAll(selector);
            if (elements.length > 0) {
                jQuery(selector).change();
                return true;
            }
            return null;
          }, [selector], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to change elements by selector "' + selector + '"');
          });
    });
  };

  Command.prototype.fillElement = function (selector, value) {
    return new this.constructor(this, function () {
      return this.parent
          .then(pollUntil(function (selector, value) {
            var elements = document.querySelectorAll(selector);
            if (elements.length > 0) {
                jQuery(selector).val(value);
                return true;
            }
            return null;
          }, [selector, value], DEFAULT_TIMEOUT))
          .then(function () {

          }, function () {
            assert.fail(null, null, 'failed to change elements by selector "' + selector + '"');
          });
    });
  };

  Command.prototype.submitForm = function (selector) {
    return new this.constructor(this, function () {
      return this.parent
          .execute(function (selector) {
            jQuery(selector).submit();
          }, [selector]);
    });
  };

  Command.prototype.mockFromFile = function (url, file, options) {
    var response = fs.readFileSync('src/test/json/' + file, 'utf-8');
    return new this.constructor(this, function () {
      return this.parent
          .execute(function (url, response, options) {
            return jQuery.mockjax(_.extend({ url: url, responseText: response }, options));
          }, [url, response, options]);
    });
  };

  Command.prototype.mockFromString = function (url, response, options) {
    return new this.constructor(this, function () {
      return this.parent
          .execute(function (url, response, options) {
            return jQuery.mockjax(_.extend({ url: url, responseText: response }, options));
          }, [url, response, options]);
    });
  };

  Command.prototype.clearMocks = function () {
    return new this.constructor(this, function () {
      return this.parent
          .execute(function () {
            jQuery.mockjax.clear();
          });
    });
  };

  Command.prototype.startApp = function (app, options) {
    return new this.constructor(this, function () {
      return this.parent
          .execute(function (app, options) {
            require(['apps/' + app + '/app'], function (App) {
              App.start(_.extend({ el: '#content' }, options));
            });
          }, [app, options])
          .sleep(1000);
    });
  };

  Command.prototype.open = function (hash) {
    var url = 'test/medium/base.html?' + Date.now();
    if (hash) {
      url += hash;
    }
    return new this.constructor(this, function () {
      return this.parent
          .get(require.toUrl(url))
          .mockFromString('/api/l10n/index', '{}')
          .checkElementExist('#content');
    });
  };

  Command.prototype.forceJSON = function () {
    return new this.constructor(this, function () {
      return this.parent
          .execute(function () {
            jQuery.ajaxSetup({ dataType: 'json' });
          });
    });
  };

});
