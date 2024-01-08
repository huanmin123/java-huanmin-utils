class  Ajax   {
    get(url)  {
        return new Promise(function(resolve, reject){
            var xhr = new XMLHttpRequest;
            xhr.open('get', url);
            xhr.send()
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        var res = xhr.responseText;
                        resolve(res);
                    }
                }
            }

        })
    }

    post(url, data) {
        return new Promise(function(resolve, reject){
            var xhr = new XMLHttpRequest;
            xhr.open('post', url);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.send(data)
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        var res = xhr.responseText;
                        resolve(res);
                    }
                }
            }

        })
    }

    /**
     *
     * @param url
     * @param formData
     *             //FormData可以一次性获取form表单的全部内容
     *             let form = document.getElementById('form')
     *             let formData = new FormData(form)
     * @returns {Promise<unknown>}
     */
    postFormData(url, formData) {
        return new Promise(function(resolve, reject){
            var xhr = new XMLHttpRequest;
            xhr.open('post', url);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.send(formData)
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        var res = xhr.responseText;
                        resolve(res);
                    }
                }
            }

        })

    }

    delete(url) {
        return new Promise(function(resolve, reject){
            var xhr = new XMLHttpRequest;
            xhr.open('delete', url);
            xhr.send()
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        var res = xhr.responseText;
                        resolve(res);
                    }
                }
            }

        })
    }
    put(url, data) {
        return new Promise(function(resolve, reject){
            var xhr = new XMLHttpRequest;
            xhr.open('put', url);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.send(data)
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        var res = xhr.responseText;
                        resolve(res);
                    }
                }
            }

        })
    }
}

export default  new Ajax();